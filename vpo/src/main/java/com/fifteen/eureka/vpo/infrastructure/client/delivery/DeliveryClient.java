package com.fifteen.eureka.vpo.infrastructure.client.delivery;

import com.fifteen.eureka.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryCreateRequest deliveryCreateRequest);

    @GetMapping("/api/deliveries/{deliveryId}")
    public ResponseEntity<?> getDelivery(@PathVariable UUID deliveryId);

    @DeleteMapping("/api/deliveries/{deliveryId}")
    public ApiResponse<?> deleteDelivery(@PathVariable UUID deliveryId);

}
