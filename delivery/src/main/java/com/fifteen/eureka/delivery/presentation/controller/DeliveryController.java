package com.fifteen.eureka.delivery.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.delivery.application.service.DeliveryService;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping
	public ApiResponse<?> createDelivery(@RequestBody DeliveryCreateRequest deliveryCreateRequest) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, deliveryService.createDelivery(deliveryCreateRequest));

	}

	@GetMapping
	public ApiResponse<?> getDeliveries(@PageableDefault Pageable pageable) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, deliveryService.getDeliveries(pageable));
	}

	@GetMapping("/{deliveryId}")
	public ApiResponse<?> getDelivery(@PathVariable UUID deliveryId) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, deliveryService.getDelivery(deliveryId));
	}

	@PatchMapping("/delivery-routes/{deliveryRouteId}")
	public ApiResponse<?> updateDeliveryStatus(
		@PathVariable UUID deliveryRouteId,
		@RequestHeader("X-Role") String role,
		@RequestHeader("X-User-Id") String userId,
		@RequestParam DeliveryStatus deliveryStatus
	) {
		deliveryService.updateDeliveryStatus(deliveryRouteId, Role.valueOf(role), Long.parseLong(userId), deliveryStatus);
		return ApiResponse.OK(ResSuccessCode.UPDATED);
	}

	@DeleteMapping("/{deliveryId}")
	public ApiResponse<?> deleteDelivery(@PathVariable UUID deliveryId) {
		deliveryService.deleteDelivery(deliveryId);
		return ApiResponse.OK(ResSuccessCode.DELETED);
	}
}
