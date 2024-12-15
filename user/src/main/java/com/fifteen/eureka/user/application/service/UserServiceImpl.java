package com.fifteen.eureka.user.application.service;

import static com.fifteen.eureka.user.domain.model.QUser.user;

import com.fifteen.eureka.common.auditor.AuditorAwareImpl;
import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserGetListResponseDto;
import com.fifteen.eureka.user.application.dto.UserGetResponseDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.infrastructure.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        !user.getEmail().equals(requestDto.getEmail())) {
      checkEmailInUse(requestDto.getEmail());
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

  @Override
  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public UserGetListResponseDto findAllUsers(List<Long> idList, Predicate predicate,
      Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
    if (idList != null && !idList.isEmpty()) {
      booleanBuilder.and(user.id.in(idList));
    }
    Page<User> userEntityPage = userRepository.findAll(booleanBuilder, pageable);
    return UserGetListResponseDto.of(userEntityPage);
  }


  @Override
  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public UserGetResponseDto findUserById(Long userId, String currentUsername, Role role) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));

    // ROLE_ADMIN_MASTER : 모든 사용자 정보 조회 가능
    if (role != Role.ROLE_ADMIN_MASTER) {
      // 나머지 : 자신의 정보만 조회 가능
      if (!user.getUsername().equals(currentUsername)) {
        throw new CustomApiException(ResErrorCode.FORBIDDEN, "Access denied.");
      }
    }
    return UserGetResponseDto.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .slackId(user.getSlackId())
        .role(user.getRole())
        .build();
  }

  @Override
  @org.springframework.transaction.annotation.Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));
    userRepository.delete(user);
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