package com.fifteen.eureka.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.delivery.application.dto.user.UserResponse;

@FeignClient(name = "user-service")
public interface UserClient {

	@GetMapping("/api/users/{userId}")
	ApiResponse<UserResponse> getUser(@PathVariable Long userId);
}
