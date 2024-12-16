package com.fifteen.eureka.user.infrastructure.client;

import com.fifteen.eureka.user.application.dto.DeliveryManagerCreateRequest;
import com.fifteen.eureka.user.infrastructure.config.FeignHeaderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service", configuration = FeignHeaderConfig.class)
public interface DeliveryManagerClient {

  @PostMapping("/api/delivery-managers")
  ResponseEntity<?> createDeliveryManager(@RequestBody DeliveryManagerCreateRequest request);
}
