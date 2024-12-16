package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.user.application.dto.AuthInfoResponseDto;
import com.fifteen.eureka.user.application.dto.LoginRequestDto;
import com.fifteen.eureka.user.application.dto.LoginResponseDto;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.domain.repository.AuthRepository;
import com.fifteen.eureka.user.domain.repository.UserRepository;
import com.fifteen.eureka.user.infrastructure.jwt.JwtTokenFactory;
import com.fifteen.eureka.user.infrastructure.jwt.JwtTokenParser;
import com.fifteen.eureka.user.infrastructure.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenFactory jwtTokenFactory;
  private final JwtTokenParser jwtTokenParser;
  private final JwtTokenValidator jwtTokenValidator;
  private final AuthRepository authRepository;

  @Override
  @Transactional
  public LoginResponseDto login(LoginRequestDto request) {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "Username not found."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Invalid username or password");
    }

    // 승인된 회원 검증
    if (user.getApprovalStatus() != ApprovalStatus.APPROVED) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "Account is not approved.");
    }

    // 토큰 생성
    String accessToken = jwtTokenFactory.createAccessToken(user.getId(), user.getUsername(),
        user.getRole());
    String refreshToken = jwtTokenFactory.createRefreshToken(user.getId());

    // 리프레시 토큰 저장
    boolean isSaved = authRepository.saveRefreshToken(user.getId(), refreshToken);
    if (!isSaved) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "Failed to store refresh token in Redis");
    }
    return new LoginResponseDto(accessToken, refreshToken);
  }


  @Override
  @Transactional
  public void logout(String accessToken) {
    Long userId = jwtTokenParser.getUserId(accessToken);

    // 리프레시 토큰 삭제
    boolean tokenDeleted = authRepository.deleteRefreshToken(userId);
    if (!tokenDeleted) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "User is already logged out.");
    }

    // 액세스 토큰 남은시간 계산
    long remainingExpiration = jwtTokenParser.getExpiration(accessToken) - System.currentTimeMillis();
    if (remainingExpiration <= 0) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token has already expired");
    }

    // 액세스 토큰 블랙리스트 추가
    boolean isBlacklisted = authRepository.addToBlacklist(accessToken, remainingExpiration);
    if (!isBlacklisted) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "Failed to add access token to blacklist");
    }
  }

  @Override
  @Transactional
  public String refreshAccessToken(String refreshToken) {
    // 리프레시 토큰 유효성 검증
    jwtTokenValidator.validateRefreshToken(refreshToken);

    // 유저정보 가져와 액세스 토큰 생성
    Long userId = jwtTokenParser.getUserId(refreshToken);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found"));

    return jwtTokenFactory.createAccessToken(user.getId(), user.getUsername(), user.getRole());
  }

  @Override
  @Transactional
  public AuthInfoResponseDto validateToken(String accessToken) {
    // 블랙리스트 확인
    if (authRepository.isBlacklisted(accessToken)) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token is blacklisted");
    }
    // 액세스 토큰 유효성 검증
    jwtTokenValidator.validateAccessToken(accessToken);

    // 검증된 유저정보 반환
    Long userId = jwtTokenParser.getUserId(accessToken);
    String username = jwtTokenParser.getUsername(accessToken);
    String role = jwtTokenParser.getRole(accessToken);

    return new AuthInfoResponseDto(userId, username, role);
  }
}

