package com.fifteen.eureka.delivery.application.dto.user;

import com.fifteen.eureka.delivery.common.role.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
	private String username;
	private String email;
	private String slackId;
	private Role role;
}
