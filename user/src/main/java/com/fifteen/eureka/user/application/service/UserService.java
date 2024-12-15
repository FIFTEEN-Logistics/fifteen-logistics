package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;

public interface UserService {

  void registerUser(SignupRequestDto signupRequestDto);

  void updateApprovalStatus(Long userId, ApprovalRequestDto requestDto);

}
