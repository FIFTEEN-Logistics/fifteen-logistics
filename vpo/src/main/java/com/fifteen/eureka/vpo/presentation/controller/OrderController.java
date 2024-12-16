package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.vpo.application.dto.order.OrderResponse;
import com.fifteen.eureka.vpo.application.service.OrderService;
import com.fifteen.eureka.vpo.infrastructure.util.PagingUtil;
import com.fifteen.eureka.vpo.presentation.request.order.CreateOrderRequest;
import com.fifteen.eureka.vpo.presentation.request.order.UpdateOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<?> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {

        return ApiResponse.OK(ResSuccessCode.CREATED, orderService.createOrder(
                request.toDto(),
                request.getOrderDetails().stream().map(CreateOrderRequest.CreateOrderDetailRequest::toDto).toList(),
                request.getDelivery().toDto(),
                currentUserId
        ));
    }

    @GetMapping
    public ApiResponse<Page<OrderResponse>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        return ApiResponse.OK(ResSuccessCode.SUCCESS, orderService.getOrders(pageable, keyword, currentUserId, currentRole));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<?> getOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, orderService.getOrder(orderId, currentUserId, currentRole));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB"})
    @PutMapping("/{orderId}")
    public ApiResponse<?> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateOrderRequest request,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, orderService.updateOrder(
                orderId,
                request.toDto(),
                currentUserId,
                currentRole
        ));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB"})
    @PutMapping("/{orderId}/cancel")
    public ApiResponse<?> cancelOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.UPDATED, orderService.cancelOrder(orderId, currentUserId, currentRole));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB"})
    @DeleteMapping("/{orderId}")
    public ApiResponse<?> deleteOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.DELETED, orderService.deleteOrder(orderId, currentUserId, currentRole));
    }
}
