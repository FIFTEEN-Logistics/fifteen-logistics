package com.fifteen.eureka.user.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.user.infrastructure.config.FilterConfig;
import com.fifteen.eureka.user.infrastructure.jwt.JwtTokenValidator;
import com.fifteen.eureka.user.infrastructure.repository.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenValidator jwtTokenValidator;
  private final RedisTokenRepository redisTokenRepository;

  // 필터 제외 경로
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return FilterConfig.EXCLUDED_PATHS.contains(path);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String path = request.getRequestURI();

      // /api/auth/refresh는 게이트웨이에서만 접근 가능하도록 제한
      if ("/api/auth/refresh".equals(path)) {
        String gatewayHeader = request.getHeader("X-Gateway");
        if (gatewayHeader == null || !gatewayHeader.equals("true")) {
          throw new CustomApiException(
              ResErrorCode.FORBIDDEN, "Access to /api/auth/refresh is not allowed from outside the gateway");
        }
      }

      // Authorization 헤더에서 토큰 추출
      String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response); // 인증이 필요 없는 요청은 통과
        return;
      }

      String accessToken = authHeader.substring(7);

      // 블랙리스트 확인
      if (redisTokenRepository.isBlacklisted(accessToken)) {
        throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token is blacklisted");
      }

      // 액세스 토큰 유효성 검증
      jwtTokenValidator.validateAccessToken(accessToken);

      // SecurityContext 설정
      Claims claims = jwtTokenValidator.extractClaims(accessToken);
      setAuthentication(request, claims);

      filterChain.doFilter(request, response); // 다음 필터로 진행

    } catch (ExpiredJwtException e) {
      handleException(response, new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token has expired"));
    } catch (CustomApiException e) {
      handleException(response, e);
    }
  }

  // SecurityContext 설정
  private void setAuthentication(HttpServletRequest request, Claims claims) {
    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
        Long.parseLong(claims.getSubject()),
        claims.get("username", String.class),
        claims.get("role", String.class),
        null
    );
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    authentication.setAuthenticated(true);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  // 예외 처리 로직
  private void handleException(HttpServletResponse response, CustomApiException ex) throws IOException {
    response.setStatus(ex.getErrorCode().getHttpStatusCode());
    response.setContentType("application/json");

    ApiResponse<?> errorResponse = ApiResponse.ERROR(
        ex.getErrorCode(),
        ex.getMessage()
    );

    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
  }
}