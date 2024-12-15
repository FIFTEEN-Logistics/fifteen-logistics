package com.fifteen.eureka.user.infrastructure.security;

import java.util.Collections;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Long userId;
  @Getter
  private final String username;
  @Getter
  private final String role;

  public JwtAuthenticationToken(Long userId, String username, String role, Object credentials) {
    super(Collections.emptyList());
    this.userId = userId;
    this.username = username;
    this.role = role;
    setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return userId;
  }
}
