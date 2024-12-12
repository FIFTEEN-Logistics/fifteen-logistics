package com.fifteen.eureka.vpo.infrastructure.client;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/api/hubs/{hubId}")
    public ApiResponse<?> getHub(@PathVariable UUID hubId);
}
