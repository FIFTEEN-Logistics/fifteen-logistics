package com.fifteen.eureka.vpo.application.dto.order;

import com.fifteen.eureka.vpo.domain.model.OrderDetail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    private String orderDetailId;
    private UUID productId;
    private String productName;
    private int quantity;
    private int productsPrice;

    public static OrderDetailResponse of(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .productId(orderDetail.getProduct().getProductId())
                .productName(orderDetail.getProduct().getProductName())
                .quantity(orderDetail.getQuantity())
                .productsPrice(orderDetail.getProduct().getProductPrice())
                .build();

    }

}
