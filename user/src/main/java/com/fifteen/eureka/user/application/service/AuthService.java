package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.user.application.dto.auth.AuthInfoResponseDto;
import com.fifteen.eureka.user.application.dto.auth.LoginRequestDto;
import com.fifteen.eureka.user.application.dto.auth.LoginResponseDto;

public interface AuthService {

  LoginResponseDto login(LoginRequestDto request);

  void logout(String accessToken);

  String refreshAccessToken(String refreshToken);

  AuthInfoResponseDto validateToken(String accessToken);
}
