package com.fifteen.eureka.user.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

  private final JwtTokenValidator validator;

  public JwtTokenParser(JwtTokenValidator validator) {
    this.validator = validator;
  }

  // 토큰에서 userId 추출
  public Long getUserId(String token) {
    Claims claims = validator.extractClaims(token);
    return Long.parseLong(claims.getSubject());
  }

  // 토큰에서 username 추출
  public String getUsername(String token) {
    return validator.extractClaims(token).get("username", String.class);
  }

  // 토큰에서 role 추출
  public String getRole(String token) {
    return validator.extractClaims(token).get("role", String.class);
  }

  // 토큰의 만료 시간 반환
  public long getExpiration(String token) {
    return validator.extractClaims(token).getExpiration().getTime();
  }
}

