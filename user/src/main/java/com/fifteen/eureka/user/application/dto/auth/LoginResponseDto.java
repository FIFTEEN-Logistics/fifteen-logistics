package com.fifteen.eureka.user.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
  private String accessToken;
  private String refreshToken;
}

