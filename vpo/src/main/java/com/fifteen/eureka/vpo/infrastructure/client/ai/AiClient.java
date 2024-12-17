package com.fifteen.eureka.vpo.infrastructure.client.ai;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AiClient {
    @PostMapping("/api/ai")
    public ResponseEntity<String> createDeliveryEstimatedTime(
            @Valid @RequestBody AiCreateDeliveryEstimatedTimeRequest aiCreateDeliveryEstimatedTimeRequest);
}
