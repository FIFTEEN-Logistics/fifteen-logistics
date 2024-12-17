package com.fifteen.eureka.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateOrderDetailDto {
    private UUID productId;
    private int quantity;

    public static CreateOrderDetailDto create(UUID productId, Integer quantity) {
        return CreateOrderDetailDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
