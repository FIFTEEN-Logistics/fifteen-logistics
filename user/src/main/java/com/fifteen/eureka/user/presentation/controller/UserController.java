package com.fifteen.eureka.user.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.user.application.dto.ApprovalRequestDto;
import com.fifteen.eureka.user.application.dto.SignupRequestDto;
import com.fifteen.eureka.user.application.dto.UserGetListResponseDto;
import com.fifteen.eureka.user.application.dto.UserGetResponseDto;
import com.fifteen.eureka.user.application.dto.UserUpdateRequestDto;
import com.fifteen.eureka.user.application.service.UserService;
import com.fifteen.eureka.user.domain.model.User;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @RoleCheck("ROLE_ADMIN_MASTER")
  @GetMapping("/users")
  public ResponseEntity<ApiResponse<UserGetListResponseDto>> findAllUsers(
      @RequestParam(required = false) List<Long> idList,
      @QuerydslPredicate(root = User.class) Predicate predicate,
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.FETCHED,
        userService.findAllUsers(idList, predicate, pageable)));
  }


  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<UserGetResponseDto>> findUserById(
      @PathVariable Long userId,
      @RequestHeader("X-Username") String currentUsername,
      @RequestHeader("X-Role") String currentRole) {

    Role role = Role.valueOf(currentRole);
    UserGetResponseDto user = userService.findUserById(userId, currentUsername, role);

    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.FETCHED, user));
  }
}
