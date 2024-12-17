package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.common.auditor.AuditorAwareImpl;
import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.user.application.dto.auth.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerCreateRequest.DeliveryManagerType;
import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerUpdateRequest;
import com.fifteen.eureka.user.application.dto.user.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.user.UserGetListResponseDto;
import com.fifteen.eureka.user.application.dto.user.UserGetResponseDto;
import com.fifteen.eureka.user.application.dto.user.UserUpdateRequestDto;
import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import com.fifteen.eureka.user.domain.model.QUser;
import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.domain.repository.UserRepository;
import com.fifteen.eureka.user.infrastructure.client.DeliveryManagerClient;
import com.fifteen.eureka.user.infrastructure.config.FeignHeaderConfig;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import feign.FeignException;
import org.springframework.transaction.annotation.Transactional;
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
  private final DeliveryManagerClient deliveryManagerClient;

  @Override
  @Transactional
  public void registerUser(SignupRequestDto requestDto) {
    checkUsernameInUse(requestDto.getUsername());
    checkEmailInUse(requestDto.getEmail());

    Role userRole = requestDto.getRole();

    // ROLE_ADMIN_MASTER 가입 제한
    if (userRole == Role.ROLE_ADMIN_MASTER) {
      throw new IllegalArgumentException("ROLE_ADMIN_MASTER cannot register via signup.");
    }

    // ROLE_DELIVERY_VENDOR일 경우 허브 ID 검증
    if (userRole == Role.ROLE_DELIVERY_VENDOR && requestDto.getHubId() == null) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Hub ID is required");
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

      // 헤더에 Username 추가 (for Auditor)
      FeignHeaderConfig.setUsername(user.getUsername());

      // ROLE_DELIVERY_HUB 또는 ROLE_DELIVERY_VENDOR일 경우 DeliveryManager 생성
      if (userRole == Role.ROLE_DELIVERY_HUB || userRole == Role.ROLE_DELIVERY_VENDOR) {
        DeliveryManagerType deliveryManagerType =
            userRole == Role.ROLE_DELIVERY_HUB
                ? DeliveryManagerType.HUB
                : DeliveryManagerType.VENDOR;

        DeliveryManagerCreateRequest deliveryManagerCreateRequest = DeliveryManagerCreateRequest.builder()
            .userId(user.getId())
            .hubId(userRole == Role.ROLE_DELIVERY_VENDOR ? requestDto.getHubId() : null)
            .deliveryManagerType(deliveryManagerType)
            .build();

        try {
          deliveryManagerClient.createDeliveryManager(deliveryManagerCreateRequest);
        } catch (FeignException e) {
          throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
              "Failed to create DeliveryManager: " + e.getMessage());
        }
      }

    } finally {
      // Thread-local 값 초기화
      AuditorAwareImpl.clearManualAuditor();
      FeignHeaderConfig.clearContext();
    }
  }

  @Override
  @Transactional
  public void updateApprovalStatus(Long userId, ApprovalRequestDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));

    ApprovalStatus newStatus = requestDto.getApprovalStatus();

    user.updateApprovalStatus(newStatus);
  }


  @Override
  @Transactional
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

    Role previousRole = user.getRole(); // 기존 역할 저장

    // 유저 정보 업데이트
    user.updateUserInfo(
        requestDto.getEmail(),
        requestDto.getSlackId(),
        requestDto.getRole(),
        requestDto.getPassword(),
        passwordEncoder
    );

    userRepository.save(user);

    // DeliveryManager 정보 업데이트
    Role updatedRole = requestDto.getRole();
    boolean wasDeliveryRole = isDeliveryRole(previousRole);
    boolean isNowDeliveryRole = isDeliveryRole(updatedRole);

    // ROLE_DELIVERY_HUB일 경우 허브 ID 검증
    if (updatedRole == Role.ROLE_DELIVERY_VENDOR && requestDto.getHubId() == null) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Hub ID is required");
    }

    if (isNowDeliveryRole && !wasDeliveryRole) {
      DeliveryManagerType deliveryManagerType =
          updatedRole == Role.ROLE_DELIVERY_HUB
              ? DeliveryManagerType.HUB
              : DeliveryManagerType.VENDOR;

      DeliveryManagerCreateRequest deliveryManagerCreateRequest = DeliveryManagerCreateRequest.builder()
          .userId(user.getId())
          .hubId(updatedRole == Role.ROLE_DELIVERY_VENDOR ? requestDto.getHubId() : null)
          .deliveryManagerType(deliveryManagerType)
          .build();

      try {
        deliveryManagerClient.createDeliveryManager(deliveryManagerCreateRequest);
      } catch (FeignException e) {
        throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
            "Failed to create DeliveryManager: " + e.getMessage());
      }
    }

    if (!isNowDeliveryRole && wasDeliveryRole) {
      // role : delivery -> other 이면, 삭제
      try {
        deliveryManagerClient.deleteDeliveryManager(user.getId());
      } catch (FeignException e) {
        throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
            "Failed to delete DeliveryManager: " + e.getMessage());
      }
    }

    if (isNowDeliveryRole) {
      // 기존 DeliveryManager 수정
      DeliveryManagerType deliveryManagerType =
          updatedRole == Role.ROLE_DELIVERY_HUB
              ? DeliveryManagerType.HUB
              : DeliveryManagerType.VENDOR;

      DeliveryManagerUpdateRequest deliveryManagerUpdateRequest = DeliveryManagerUpdateRequest.builder()
          .hubId(updatedRole == Role.ROLE_DELIVERY_VENDOR ? requestDto.getHubId() : null)
          .deliveryManagerType(deliveryManagerType)
          .build();

      try {
        deliveryManagerClient.updateDeliveryManager(user.getId(), deliveryManagerUpdateRequest);
      } catch (FeignException e) {
        throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
            "Failed to update DeliveryManager: " + e.getMessage());
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserGetListResponseDto findAllUsers(List<Long> idList, Predicate predicate,
      Pageable pageable) {

    QUser qUser = QUser.user;
    BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
    if (idList != null && !idList.isEmpty()) {
      booleanBuilder.and(qUser.id.in(idList));
    }
    Page<User> userEntityPage = userRepository.findAll(booleanBuilder, pageable);
    return UserGetListResponseDto.of(userEntityPage);
  }


  @Override
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public UserGetResponseDto findUserByIdForService(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));

    return UserGetResponseDto.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .slackId(user.getSlackId())
        .role(user.getRole())
        .build();
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "User not found."));

    user.updateApprovalStatus(ApprovalStatus.REJECTED);
    user.markAsDeleted();

    try {
      deliveryManagerClient.deleteDeliveryManager(user.getId());
    } catch (FeignException e) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "Failed to delete DeliveryManager: " + e.getMessage());
    }
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

  // 딜리버리 롤인지 확인하는 메서드
  private boolean isDeliveryRole(Role role) {
    return role == Role.ROLE_DELIVERY_HUB || role == Role.ROLE_DELIVERY_VENDOR;
  }
}