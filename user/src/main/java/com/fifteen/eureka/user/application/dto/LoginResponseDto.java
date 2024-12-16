package com.fifteen.eureka.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
  private String accessToken;
  private String refreshToken;
}
