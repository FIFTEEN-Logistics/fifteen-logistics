package com.sparta.fifteen.vpo.application.dto.order;

import com.sparta.fifteen.vpo.domain.model.Order;
import com.sparta.fifteen.vpo.domain.model.OrderDetail;
import com.sparta.fifteen.vpo.presentation.request.order.CreateOrderRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OrderResponse {
    private UUID orderId;
    private long totalPrice;
    private Long userId;
    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;
    private List<OrderDetailResponse> orderDetails;

    //delivery는 나중에 실제 딜리버리로 바꾸김 + orderdetails동
    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUserId())
                .supplierId(order.getSupplier().getVendorId())
                .receiverId(order.getReceiver().getVendorId())
                .orderRequest(order.getOrderRequest())
                .orderDetails(order.getOrderDetails().stream().map(OrderDetailResponse::of).toList())
                .build();
    }

}