package com.fifteen.eureka.user.infrastructure.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignHeaderConfig {

  private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();

  // ThreadLocal에 사용자 이름 설정 (회원가입용)
  public static void setUsername(String username) {
    USERNAME_HOLDER.set(username);
  }

  // ThreadLocal 값 초기화
  public static void clearContext() {
    USERNAME_HOLDER.remove();
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      // ThreadLocal에서 사용자 이름 가져오기
      String usernameFromThreadLocal = USERNAME_HOLDER.get();

      // HttpServletRequest에서 헤더 값 가져오기
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      String username = null;

      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        String userId = request.getHeader("X-UserId");
        String usernameFromHeader = request.getHeader("X-Username");
        String userRole = request.getHeader("X-Role");
        String accessToken = request.getHeader("Authorization");

        // 가져온 헤더 설정
        if (userId != null) {
          requestTemplate.header("X-UserId", userId);
        }
        if (usernameFromHeader != null) {
          username = usernameFromHeader;
          requestTemplate.header("X-Username", username);
        }
        if (userRole != null) {
          requestTemplate.header("X-Role", userRole);
        }
        if (accessToken != null) {
          requestTemplate.header("Authorization", accessToken);
        }
      }

      // HttpServletRequest에서 username을 가져오지 못했으면 ThreadLocal 값 사용
      if (username == null && usernameFromThreadLocal != null) {
        username = usernameFromThreadLocal;
        requestTemplate.header("X-Username", username);
        System.out.println("X-Username (ThreadLocal): " + username);
      }

      if (username == null) {
        throw new IllegalStateException(
            "Username must be provided");
      }
    };
  }
}
