package com.fifteen.eureka.user.infrastructure.jwt;

import com.fifteen.eureka.common.role.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Component
public class JwtTokenFactory {

  private final Key signingKey;
  @Value("${jwt.access.expiration}")
  private long accessTokenValidity;
  @Value("${jwt.refresh.expiration}")
  private long refreshTokenValidity;


  public JwtTokenFactory(@Value("${jwt.secret}") String secretKey) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  // 액세스 토큰 생성
  public String createAccessToken(Long userId, String username, Role role) {
    return createToken(userId, username, role, accessTokenValidity * 1000, "access"); // 초 → 밀리초
  }

  // 리프레시 토큰 생성
  public String createRefreshToken(Long userId) {
    return createToken(userId, null, null, refreshTokenValidity * 1000, "refresh");
  }

  private String createToken(Long userId, String username, Role role, long validity, String tokenType) {
    Claims claims = Jwts.claims().setSubject(userId.toString());
    claims.put("type", tokenType);

    if (username != null) claims.put("username", username);
    if (role != null) claims.put("role", role.toString());

    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + validity))
        .signWith(signingKey)
        .compact();
  }
}

