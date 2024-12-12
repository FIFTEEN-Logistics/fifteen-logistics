package com.sparta.fifteen.vpo.presentation.controller;

import com.sparta.fifteen.vpo.application.dto.order.OrderResponse;
import com.sparta.fifteen.vpo.application.dto.product.ProductResponse;
import com.sparta.fifteen.vpo.application.service.OrderService;
import com.sparta.fifteen.vpo.presentation.request.order.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(
                request.toDto(),
                request.getOrderDetails().stream().map(CreateOrderRequest.OrderDetail::toDto).toList(),
                request.getDelivery().toDto()
        ));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(@PageableDefault(sort = "orderNumber") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

}
