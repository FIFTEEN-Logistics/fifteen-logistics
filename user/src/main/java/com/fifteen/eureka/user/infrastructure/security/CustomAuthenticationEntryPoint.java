package com.fifteen.eureka.user.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    ResErrorCode errorCode = ResErrorCode.UNAUTHORIZED;
    ApiResponse<Void> errorResponse = ApiResponse.ERROR(errorCode, authException.getMessage());
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
