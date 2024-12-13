package com.fifteen.eureka.vpo.application.dto.order;

import com.fifteen.eureka.vpo.domain.model.Order;
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
    private UUID receiverId;
    private UUID supplierId;
    private String orderRequest;
    private boolean isDeleted;
    private boolean isCanceled;

    private List<OrderDetailResponse> orderDetails;

    //delivery는 나중에 실제 딜리버리로 바꾸김 + orderdetails동
    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUserId())
                .receiverId(order.getReceiver().getVendorId())
                .supplierId(order.getSupplier().getVendorId())
                .orderRequest(order.getOrderRequest())
                .isDeleted(order.isDeleted())
                .isCanceled(order.isCanceled())
                .orderDetails(order.getOrderDetails().stream().map(OrderDetailResponse::of).toList())
                .build();
    }

}
