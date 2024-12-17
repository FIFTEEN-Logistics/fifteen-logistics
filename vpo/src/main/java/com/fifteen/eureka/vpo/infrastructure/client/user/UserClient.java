package com.fifteen.eureka.vpo.infrastructure.client.user;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/service/users/{userId}")
    public ResponseEntity<ApiResponse<UserGetResponseDto>> findUserByIdForService(
            @PathVariable Long userId);
}