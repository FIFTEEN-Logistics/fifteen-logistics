package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.common.auditor.AuditorAwareImpl;
import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void registerUser(SignupRequestDto requestDto) {
    checkEmailInUse(requestDto.getEmail());
    checkUsernameInUse(requestDto.getUsername());

    Role userRole = requestDto.getRole();

    // ROLE_ADMIN_MASTER 가입 제한
    if (userRole == Role.ROLE_ADMIN_MASTER) {
      throw new IllegalArgumentException("ROLE_ADMIN_MASTER cannot register via signup.");
    }

    // 수동 Auditor 설정
    AuditorAwareImpl.setManualAuditor(requestDto.getUsername());

    try {
      User user = new User(
          requestDto.getUsername(),
          passwordEncoder.encode(requestDto.getPassword()),
          requestDto.getEmail(),
          requestDto.getSlackId(),
          userRole
      );

      userRepository.save(user);
    } finally {
      // Thread-local 값 초기화
      AuditorAwareImpl.clearManualAuditor();
    }
  }

  @Override
  @org.springframework.transaction.annotation.Transactional
  public void updateApprovalStatus(Long userId, ApprovalRequestDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));

    ApprovalStatus newStatus = requestDto.getApprovalStatus();

    user.updateApprovalStatus(newStatus);
    userRepository.save(user);
  }

  @Override
  @org.springframework.transaction.annotation.Transactional
  public void updateUser(Long userId, UserUpdateRequestDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found"));

    // 이메일 중복 검증 (새로운 이메일이 기존 이메일과 다를 경우에만 검증)
    if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank() &&
        !user.getEmail().equals(requestDto.getEmail()) &&
        userRepository.existsByEmail(requestDto.getEmail())) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Email already in use.");
    }

    // 동일 값 검증
    List<String> unchangedFields = new ArrayList<>();
    if (requestDto.getEmail() != null && requestDto.getEmail().equals(user.getEmail())) {
      unchangedFields.add("email");
    }
    if (requestDto.getSlackId() != null && requestDto.getSlackId().equals(user.getSlackId())) {
      unchangedFields.add("slackId");
    }
    if (requestDto.getRole() != null && requestDto.getRole().equals(user.getRole())) {
      unchangedFields.add("role");
    }
    if (requestDto.getPassword() != null && passwordEncoder.matches(requestDto.getPassword(),
        user.getPassword())) {
      unchangedFields.add("password");
    }

    // 동일한 필드 예외 처리
    if (!unchangedFields.isEmpty()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST,
          "same as before: " + String.join(", ", unchangedFields));
    }

    user.updateUserInfo(
        requestDto.getEmail(),
        requestDto.getSlackId(),
        requestDto.getRole(),
        requestDto.getPassword(),
        passwordEncoder
    );

    userRepository.save(user);
  }

  // email 중복 체크 메서드
  private void checkEmailInUse(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Email already in use.");
    }
  }

  // username 중복 체크 메서드
  private void checkUsernameInUse(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Username already in use.");
    }
  }
}