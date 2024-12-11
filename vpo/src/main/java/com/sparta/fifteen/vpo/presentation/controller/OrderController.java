package com.sparta.fifteen.vpo.presentation.controller;

import com.sparta.fifteen.vpo.application.service.OrderService;
import com.sparta.fifteen.vpo.presentation.request.order.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
