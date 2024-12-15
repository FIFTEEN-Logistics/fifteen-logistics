package com.fifteen.eureka.user.application.service;

import com.fifteen.eureka.user.application.dto.SignupRequestDto;

public interface UserService {

  void registerUser(SignupRequestDto signupRequestDto);
}
