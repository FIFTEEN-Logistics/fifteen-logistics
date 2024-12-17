package com.fifteen.eureka.user.infrastructure.client;

import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerUpdateRequest;
import com.fifteen.eureka.user.infrastructure.config.FeignHeaderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service", configuration = FeignHeaderConfig.class)
public interface DeliveryManagerClient {

  @PostMapping("/api/delivery-managers")
  ResponseEntity<?> createDeliveryManager(@RequestBody DeliveryManagerCreateRequest request);

  @PutMapping("/api/delivery-managers/{userId}")
  ResponseEntity<?> updateDeliveryManager(
      @PathVariable Long userId,
      @RequestBody DeliveryManagerUpdateRequest request);

  @DeleteMapping("/api/delivery-managers/{userId}")
  ResponseEntity<?> deleteDeliveryManager(@PathVariable Long userId);
}

