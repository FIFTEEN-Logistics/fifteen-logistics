package com.fifteen.eureka.user.presentation.controller;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.user.application.dto.AuthInfoResponseDto;
import com.fifteen.eureka.user.application.dto.LoginRequestDto;
import com.fifteen.eureka.user.application.dto.LoginResponseDto;
import com.fifteen.eureka.user.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {
    LoginResponseDto response = authService.login(request);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.FETCHED, response));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED,
          "Authorization header is missing or invalid");
    }

    String accessToken = authorizationHeader.substring(7);
    authService.logout(accessToken);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.SUCCESS, "logout"));
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(
      @RequestHeader("Refresh-Token") String refreshToken) {
    String newAccessToken = authService.refreshAccessToken(refreshToken);
    return ResponseEntity.ok(newAccessToken);
  }

  @PostMapping("/validate-token")
  public ResponseEntity<ApiResponse<AuthInfoResponseDto>> validateToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    String accessToken = authorizationHeader.substring(7);
    AuthInfoResponseDto userInfo = authService.validateToken(accessToken);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.SUCCESS, userInfo));
  }
}
