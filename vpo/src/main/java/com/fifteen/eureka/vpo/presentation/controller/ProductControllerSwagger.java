package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.presentation.request.product.CreateProductRequest;
import com.fifteen.eureka.vpo.presentation.request.product.UpdateProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Product API", description = "Product 관리 API")
public interface ProductControllerSwagger {

    @Operation(summary = "Product 생성", description = "Product를 생성합니다.")
    @PostMapping
    ApiResponse<?> createProduct(
            @Parameter(description = "Product 생성 요청 DTO") @RequestBody CreateProductRequest request,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);
    @Operation(summary = "Product 목록 조회", description = "키워드로 Product 목록을 조회합니다.")
    @GetMapping
    ApiResponse<PagedModel<ProductResponse>> getProducts(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 여부") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);
    @Operation(summary = "Product 상세 조회", description = "특정 Product를 조회합니다.")
    @GetMapping("/{productId}")
    ApiResponse<?> getProduct(
            @Parameter(description = "Product ID") @PathVariable UUID productId,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Product 수정", description = "Product를 수정합니다.")
    @PutMapping("/{productId}")
    ApiResponse<?> updateProduct(
            @Parameter(description = "Product ID") @PathVariable UUID productId,
            @Parameter(description = "Product 수정 요청 DTO") @RequestBody UpdateProductRequest request,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Product 삭제", description = "Product를 삭제합니다.")
    @DeleteMapping("/{productId}")
    ApiResponse<?> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable UUID productId,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);
}
