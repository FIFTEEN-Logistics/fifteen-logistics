package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.vpo.application.dto.order.OrderResponse;
import com.fifteen.eureka.vpo.presentation.request.order.CreateOrderRequest;
import com.fifteen.eureka.vpo.presentation.request.order.UpdateOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Order API", description = "Order 관리 API")
public interface OrderControllerSwagger {

    @Operation(summary = "Order 생성", description = "Order를 생성합니다.")
    @PostMapping
    ApiResponse<?> createOrder(
            @Parameter(description = "Order 생성 요청 DTO") @RequestBody CreateOrderRequest request,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Order 목록 조회", description = "키워드로 Order 목록을 조회합니다.")
    @GetMapping
    ApiResponse<PagedModel<OrderResponse>> getOrders(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 여부") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Order 상세 조회", description = "특정 Order를 조회합니다.")
    @GetMapping("/{orderId}")
    ApiResponse<?> getOrder(
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Order 수정", description = "Order를 수정합니다.")
    @PutMapping("/{orderId}")
    ApiResponse<?> updateOrder(
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Parameter(description = "Order 수정 요청 DTO") @RequestBody UpdateOrderRequest request,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Order 취소", description = "Order를 취소합니다.")
    @PutMapping("/{orderId}/cancel")
    ApiResponse<?> cancelOrder(
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);

    @Operation(summary = "Order 삭제", description = "Order를 삭제합니다.")
    @DeleteMapping("/{orderId}")
    ApiResponse<?> deleteOrder(
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Parameter(description = "로그인 유저 ID") @RequestHeader("X-UserId") Long currentUserId,
            @Parameter(description = "로그인 유저 권한") @RequestHeader("X-Role") String currentRole);
}
