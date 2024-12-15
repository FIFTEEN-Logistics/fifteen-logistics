package com.fifteen.eureka.user.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;
import com.fifteen.eureka.user.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users/signup")
  public ResponseEntity<ApiResponse<Void>> registerUser(
      @RequestBody @Valid SignupRequestDto signupRequestDto) {
    userService.registerUser(signupRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "signup"));
  }

  @RoleCheck("ROLE_ADMIN_MASTER")
  @PatchMapping("/users/{userId}/approve")
  public ResponseEntity<ApiResponse<Void>> updateApprovalStatus(@PathVariable Long userId,
      @Valid @RequestBody ApprovalRequestDto requestDto) {
    userService.updateApprovalStatus(userId, requestDto);
    return ResponseEntity.ok().body(ApiResponse.OK(ResSuccessCode.UPDATED, "Approval status"));
  }

  @RoleCheck("ROLE_ADMIN_MASTER")
  @PatchMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequestDto updateRequestDto) {

    userService.updateUser(userId, updateRequestDto);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.UPDATED));
  }
}
