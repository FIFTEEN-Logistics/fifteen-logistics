package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.presentation.request.vendor.CreateVendorRequest;
import com.fifteen.eureka.vpo.presentation.request.vendor.UpdateVendorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Vendor API", description = "Vendor 관리 API")
public interface VendorControllerSwagger {

    @Operation(summary = "Vendor 생성", description = "Vendor를 생성합니다.")
    @PostMapping
    ApiResponse<?> createVendor(
            @Parameter(description = "Vendor 생성 요청 DTO") @RequestBody CreateVendorRequest request,
            @Parameter(description = "사용자 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "사용자 역할") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Vendor 목록 조회", description = "키워드로 Vendor 목록을 조회합니다.")
    @GetMapping
    ApiResponse<PagedModel<VendorResponse>> getVendors(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 여부") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "사용자 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "사용자 역할") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Vendor 상세 조회", description = "특정 Vendor를 조회합니다.")
    @GetMapping("/{vendorId}")
    ApiResponse<?> getVendor(
            @Parameter(description = "Vendor ID") @PathVariable UUID vendorId,
            @Parameter(description = "사용자 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "사용자 역할") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Vendor 수정", description = "Vendor를 수정합니다.")
    @PutMapping("/{vendorId}")
    ApiResponse<?> updateVendor(
            @Parameter(description = "Vendor ID") @PathVariable UUID vendorId,
            @Parameter(description = "Vendor 수정 요청 DTO") @RequestBody UpdateVendorRequest request,
            @Parameter(description = "사용자 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "사용자 역할") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Vendor 삭제", description = "Vendor를 삭제합니다.")
    @DeleteMapping("/{vendorId}")
    ApiResponse<?> deleteVendor(
            @Parameter(description = "Vendor ID") @PathVariable UUID vendorId,
            @Parameter(description = "사용자 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "사용자 역할") @RequestHeader("X-Role") String currentRole);
}
