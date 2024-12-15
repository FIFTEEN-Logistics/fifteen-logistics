package com.fifteen.eureka.gateway.filter;

import com.fifteen.eureka.gateway.exception.GatewayException;
import com.fifteen.eureka.gateway.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

  @Value("${jwt.secret}")
  private String secretKey;

  private final WebClient.Builder webClientBuilder;
  private final RedisTokenRepository redisTokenRepository;

  // 필터 제외 경로
  private static final List<String> EXCLUDED_PATHS = List.of(
      "/api/auth/login",
      "/api/users/signup",
      "/api/auth/refresh"
  );

  public JwtFilter(WebClient.Builder webClientBuilder, RedisTokenRepository redisTokenRepository) {
    super(Config.class);
    this.webClientBuilder = webClientBuilder;
    this.redisTokenRepository = redisTokenRepository;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      try {
        // 요청 경로 추출
        String path = exchange.getRequest().getPath().toString();

        // 검증 제외 경로일 경우 필터 통과
        if (EXCLUDED_PATHS.contains(path)) {
          return chain.filter(exchange);
        }

        // Authorization 헤더 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          throw new GatewayException(HttpStatus.UNAUTHORIZED,
              "Missing or invalid Authorization header");
        }

        String accessToken = authHeader.substring(7);

        // 블랙리스트 확인
        if (redisTokenRepository.isBlacklisted(accessToken)) {
          throw new GatewayException(HttpStatus.UNAUTHORIZED, "Access token is blacklisted");
        }

        // 액세스 토큰 검증
        validateToken(accessToken);

        // 유저정보 및 액세스 토큰 헤더에 추가 및 체인 진행
        return addHeaders(exchange, chain, accessToken);
      } catch (ExpiredJwtException ex) {
        // 액세스 토큰 만료 시 리프레시 토큰으로 액세스 토큰 재발급 요청 처리
        String refreshToken = exchange.getRequest().getHeaders().getFirst("Refresh-Token");
        if (refreshToken != null) {
          return handleRefreshToken(exchange, chain, refreshToken);
        }
        throw new GatewayException(HttpStatus.UNAUTHORIZED, "Access token has expired");
      } catch (GatewayException ex) {
        throw ex;
      } catch (Exception ex) {
        throw new GatewayException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
      }
    };
  }

  // 리프레시 토큰으로 액세스 토큰 재발급 요청 처리
  private Mono<Void> handleRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain,
      String refreshToken) {
    // 리프레시 토큰 유효성 검증
    if (!redisTokenRepository.isRefreshTokenValid(refreshToken)) {
      throw new GatewayException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
    }
    return webClientBuilder.build()
        .post()
        .uri("http://localhost:8080/api/auth/refresh")
        .header("Refresh-Token", refreshToken)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(newAccessToken -> {
          // 유저정보 및 액세스토큰 헤더 추가
          return addHeaders(exchange, chain, newAccessToken);
        })
        .onErrorResume(ex -> Mono.error(
            new GatewayException(HttpStatus.UNAUTHORIZED, "Failed to refresh access token")));
  }

  // 유저정보 및 액세스토큰 헤더 추가
  private Mono<Void> addHeaders(ServerWebExchange exchange, GatewayFilterChain chain,
      String accessToken) {
    Claims claims = extractClaims(accessToken);
    // 헤더에 액세스토큰도 추가
    ServerWebExchange mutatedExchange = exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header("X-UserId", claims.getSubject())
            .header("X-Username", claims.get("username", String.class))
            .header("X-Role", claims.get("role", String.class))
            .build())
        .build();

    // api 요청 헤더 정보 출력
    System.out.println("Added Headers: " + mutatedExchange.getRequest().getHeaders());

    return chain.filter(mutatedExchange); // 다음 필터 체인 실행
  }

  // JWT 토큰 유효성 검증 (액세스 토큰용)
  public void validateToken(String accessToken) {
    Claims claims = extractClaims(accessToken);

    // type 필드 존재 여부와 값 확인
    String tokenType = claims.get("type", String.class);
    if (!"access".equals(tokenType)) {
      throw new GatewayException(HttpStatus.UNAUTHORIZED, "Not an access token");
    }
  }

  // JWT에서 Claims 추출
  private Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Signing Key 가져오기
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public static class Config {

  }
}
