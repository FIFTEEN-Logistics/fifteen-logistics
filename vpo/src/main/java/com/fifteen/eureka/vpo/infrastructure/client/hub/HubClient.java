package com.fifteen.eureka.vpo.infrastructure.client.hub;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "delivery-service", contextId = "hubClient")
public interface HubClient {

    @GetMapping("/api/hubs/{hubId}")
    public ApiResponse<HubDetailsResponse> getHub(@PathVariable UUID hubId);

}
