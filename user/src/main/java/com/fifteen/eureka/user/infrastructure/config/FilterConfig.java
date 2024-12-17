package com.fifteen.eureka.user.infrastructure.config;

import java.util.List;

public class FilterConfig {
  public static final List<String> EXCLUDED_PATHS = List.of(
      "/api/users/signup",
      "/api/auth/login",
      "/api/auth/refresh",
      "/api/service/**"
  );
}
