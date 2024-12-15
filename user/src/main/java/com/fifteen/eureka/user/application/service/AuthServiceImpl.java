package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.user.application.dto.LoginRequestDto;
import com.fifteen.eureka.user.application.dto.LoginResponseDto;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.infrastructure.repository.RedisTokenRepository;
import com.fifteen.eureka.user.infrastructure.repository.UserRepository;
import com.fifteen.eureka.user.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTokenRepository redisTokenRepository;

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
    String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(),
        user.getRole());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

    // 리프레시 토큰 저장
    boolean isSaved = redisTokenRepository.saveRefreshToken(user.getId(), refreshToken);
    if (!isSaved) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "Failed to store refresh token in Redis");
    }
    return new LoginResponseDto(accessToken, refreshToken);
  }


  @Override
  @Transactional
  public void logout(String accessToken) {
    Long userId = jwtTokenProvider.getUserId(accessToken);

    // 리프레시 토큰 삭제
    boolean tokenDeleted = redisTokenRepository.deleteRefreshToken(userId);
    if (!tokenDeleted) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "User is already logged out.");
    }

    // 액세스 토큰 남은시간 계산
    long remainingExpiration = jwtTokenProvider.getExpiration(accessToken) - System.currentTimeMillis();
    if (remainingExpiration <= 0) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token has already expired");
    }

    // 액세스 토큰 블랙리스트 추가
    boolean isBlacklisted = redisTokenRepository.addToBlacklist(accessToken, remainingExpiration);
    if (!isBlacklisted) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "Failed to add access token to blacklist");
    }
  }

  @Override
  @Transactional
  public String refreshAccessToken(String refreshToken) {

    // 리프레시 토큰 유효성 검사
    Claims claims;
    try {
      claims = jwtTokenProvider.extractClaims(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Refresh token has expired");
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Invalid refresh token");
    }

    // 유저정보 가져와 액세스 토큰 생성
    Long userId = Long.parseLong(claims.getSubject());

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found"));

    return jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRole());
  }
}

