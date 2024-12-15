package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;

public interface UserService {

  void registerUser(SignupRequestDto signupRequestDto);

  void updateApprovalStatus(Long userId, ApprovalRequestDto requestDto);

  void updateUser(Long userId, UserUpdateRequestDto updateRequestDto);
}
