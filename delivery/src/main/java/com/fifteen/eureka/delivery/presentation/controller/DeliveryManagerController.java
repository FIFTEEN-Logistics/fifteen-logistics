package com.fifteen.eureka.delivery.presentation.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fifteen.eureka.delivery.common.response.ResSuccessCode;
import com.fifteen.eureka.delivery.application.service.DeliveryManagerService;
import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.presentation.request.DeliveryManagerCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> createDeliveryManager(@RequestBody DeliveryManagerCreateRequest deliveryManagerCreateRequest) {
		Long id = deliveryManagerService.createDeliveryManager(deliveryManagerCreateRequest.toDto()).getId();
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.OK(ResSuccessCode.CREATED));
	}
}
