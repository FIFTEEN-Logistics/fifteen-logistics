package com.fifteen.eureka.delivery.presentation.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateRequest;
import com.fifteen.eureka.delivery.application.dto.hub.HubUpdateRequest;
import com.fifteen.eureka.delivery.application.service.HubService;
import com.fifteen.eureka.delivery.common.response.ApiResponse;
import com.fifteen.eureka.delivery.common.response.ResSuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	@PostMapping
	public ResponseEntity<ApiResponse<?>> createHub(@RequestBody HubCreateRequest hubCreateRequest) {
		UUID createdHubId = hubService.createHub(hubCreateRequest).getId();
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(createdHubId)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.OK(ResSuccessCode.CREATED));
	}

	@GetMapping
	public ApiResponse<?> getHubs(
		@RequestParam(required = false) String search,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, hubService.getHubs(pageable, search));
	}

	@GetMapping("/{hubId}")
	public ApiResponse<?> getHub(@PathVariable UUID hubId) {
		return ApiResponse.OK(ResSuccessCode.SUCCESS, hubService.getHub(hubId));
	}

	@PutMapping("/{hubId}")
	public ApiResponse<?> updateHub(
		@PathVariable UUID hubId,
		@RequestBody HubUpdateRequest hubUpdateRequest
	) {
		hubService.updateHub(hubId, hubUpdateRequest);
		return ApiResponse.OK(ResSuccessCode.UPDATED);
	}

	@DeleteMapping("/{hubId}")
	public ApiResponse<?> deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(hubId);
		return ApiResponse.OK(ResSuccessCode.DELETED);
	}
}
