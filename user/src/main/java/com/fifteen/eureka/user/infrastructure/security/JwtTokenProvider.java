package com.fifteen.eureka.user.infrastructure.security;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.access.expiration}")
  private long accessTokenValidity;
  @Value("${jwt.refresh.expiration}")
  private long refreshTokenValidity;


  // 액세스 토큰 생성
  public String createAccessToken(Long userId, String username, Role role) {
    return createToken(userId, username, role, accessTokenValidity * 1000, "access"); // 초 → 밀리초
  }

  // 리프레시 토큰 생성
  public String createRefreshToken(Long userId) {
    return createToken(userId, null, null, refreshTokenValidity * 1000, "refresh");
  }

  // 공통 토큰 생성 로직
  private String createToken(Long userId, String username, Role role, long validity, String tokenType) {
    Claims claims = Jwts.claims().setSubject(userId.toString());
    claims.put("type", tokenType);

    if (username != null) {
      claims.put("username", username);
    }
    if (role != null) {
      claims.put("role", role.toString());
    }

    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // 토큰에서 userId 추출
  public Long getUserId(String token) {
    String subject = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    return Long.parseLong(subject);
  }

  // 토큰에서 username 추출
  public String getUsername(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    return claims.get("username", String.class);
  }

  // 토큰에서 role 추출
  public String getRole(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    return claims.get("role", String.class); // Role이 없는 경우 null 반환
  }

  // JWT 토큰 유효성 검증 (액세스 토큰용)
  public boolean validateToken(String accessToken) {
    try {
      Claims claims = extractClaims(accessToken);

      // type 필드의 존재 여부와 값 검증
      String tokenType = claims.get("type", String.class);
      if (!"access".equals(tokenType)) {
        throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Not an access token");
      }
      return true;
    } catch (ExpiredJwtException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "JWT token has expired: " + e.getMessage());
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Invalid Access Token: " + e.getMessage());
    }
  }

  // Claims 추출
  public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // 토큰의 만료 시간 반환
  public long getExpiration(String token) {
    Claims claims = extractClaims(token);
    return claims.getExpiration().getTime(); // 밀리초
  }

  // Signing Key 가져오기
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
