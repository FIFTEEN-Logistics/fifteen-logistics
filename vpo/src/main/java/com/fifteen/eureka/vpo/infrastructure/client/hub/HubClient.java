package com.fifteen.eureka.vpo.infrastructure.client.hub;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "delivery-service", contextId = "hubClient")
public interface HubClient {

    @GetMapping("/api/hubs/{hubId}")
    public ResponseEntity<HubDetailsResponse> getHub(@PathVariable UUID hubId);

}
