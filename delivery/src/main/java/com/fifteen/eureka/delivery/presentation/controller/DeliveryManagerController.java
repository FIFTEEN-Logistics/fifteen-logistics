package com.fifteen.eureka.delivery.presentation.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerUpdateRequest;
import com.fifteen.eureka.delivery.application.service.DeliveryManagerService;
import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.common.response.ResSuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> createDeliveryManager(
		@RequestBody DeliveryManagerCreateRequest deliveryManagerCreateRequest) {
		Long id = deliveryManagerService.createDeliveryManager(deliveryManagerCreateRequest).getId();
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.OK(ResSuccessCode.CREATED));
	}

	//TODO 유저 서비스 구현 후에 조회 기능 구현

	@PutMapping("/{userId}")
	public ApiResponse<?> updateDeliveryManager(
		@PathVariable Long userId,
		@RequestBody DeliveryManagerUpdateRequest deliveryManagerUpdateRequest
	) {
		deliveryManagerService.updateDeliveryManager(userId, deliveryManagerUpdateRequest);
		return ApiResponse.OK(ResSuccessCode.UPDATED);
	}

	@DeleteMapping("/{userId}")
	public ApiResponse<?> deleteDeliveryManager(@PathVariable Long userId) {
		deliveryManagerService.deleteDeliveryManager(userId);
		return ApiResponse.OK(ResSuccessCode.DELETED);
	}
}
