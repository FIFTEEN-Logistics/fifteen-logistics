package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserGetListResponseDto;
import com.fifteen.eureka.user.application.dto.UserGetResponseDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {

  void registerUser(SignupRequestDto signupRequestDto);

  void updateApprovalStatus(Long userId, ApprovalRequestDto requestDto);

  void updateUser(Long userId, UserUpdateRequestDto updateRequestDto);

  UserGetListResponseDto findAllUsers(List<Long> idList, Predicate predicate, Pageable pageable);

  UserGetResponseDto findUserById(Long userId, String currentUsername, Role currentRole);

  void deleteUser(Long userId);
}
