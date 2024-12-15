package com.fifteen.eureka.delivery.presentation.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliverySimpleResponse;
import com.fifteen.eureka.delivery.application.service.DeliveryService;
import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.common.response.ResSuccessCode;
import com.fifteen.eureka.delivery.common.role.Role;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> createDelivery(@RequestBody DeliveryCreateRequest deliveryCreateRequest) {
		UUID deliveryId = deliveryService.createDelivery(deliveryCreateRequest).getId();
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(deliveryId)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.OK(ResSuccessCode.CREATED));

	}

	@GetMapping
	public ApiResponse<?> getDeliveries(@PageableDefault Pageable pageable) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, deliveryService.getDeliveries(pageable));
	}

	@GetMapping("/{deliveryId}")
	public ApiResponse<?> getDelivery(@PathVariable UUID deliveryId) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, deliveryService.getDelivery(deliveryId));
	}
}
