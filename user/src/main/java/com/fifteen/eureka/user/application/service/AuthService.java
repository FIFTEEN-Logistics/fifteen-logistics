package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.user.application.dto.LoginRequestDto;
import com.fifteen.eureka.user.application.dto.LoginResponseDto;

public interface AuthService {

  LoginResponseDto login(LoginRequestDto request);

  void logout(String accessToken);

  String refreshAccessToken(String refreshToken);
}
