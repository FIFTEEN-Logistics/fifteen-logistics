package com.fifteen.eureka.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateOrderDto {

    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;

    public static CreateOrderDto create(UUID receiverId, UUID supplierId, String orderRequest) {
        return CreateOrderDto.builder()
                .receiverId(receiverId)
                .supplierId(supplierId)
                .orderRequest(orderRequest)
                .build();
    }
}
