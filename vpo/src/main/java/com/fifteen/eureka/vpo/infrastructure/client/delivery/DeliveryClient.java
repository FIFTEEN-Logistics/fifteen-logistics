package com.fifteen.eureka.vpo.infrastructure.client.delivery;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    public ApiResponse<?> createDelivery(@RequestBody DeliveryCreateRequest deliveryCreateRequest);

    @GetMapping("/api/deliveries/{deliveryId}")
    public ApiResponse<?> getDelivery(@PathVariable UUID deliveryId);
}
