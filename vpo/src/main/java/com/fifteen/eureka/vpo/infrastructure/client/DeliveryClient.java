package com.fifteen.eureka.vpo.infrastructure.client;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    public ResponseEntity<ApiResponse<?>> createDelivery(@RequestBody CreateDeliveryDto createDeliveryDto);
}
