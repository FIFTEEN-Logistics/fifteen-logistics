package com.fifteen.eureka.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateOrderDetailDto {

    private UUID orderDetailId;
    private UUID productId;
    private int quantity;

    public static UpdateOrderDetailDto create(UUID orderDetailId, UUID productId, Integer quantity) {
        return UpdateOrderDetailDto.builder()
                .orderDetailId(orderDetailId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
