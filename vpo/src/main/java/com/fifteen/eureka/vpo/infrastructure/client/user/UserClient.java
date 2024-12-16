package com.fifteen.eureka.vpo.infrastructure.client.user;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserGetResponseDto>> findUserById(
            @PathVariable Long userId,
            @RequestHeader("X-Username") String currentUsername,
            @RequestHeader("X-Role") String currentRole);

}