package com.fifteen.eureka.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateOrderDto {

    private Long userId;
    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;

    public static CreateOrderDto create(Long userId, UUID receiverId, UUID supplierId, String orderRequest) {
        return CreateOrderDto.builder()
                .userId(userId)
                .receiverId(receiverId)
                .supplierId(supplierId)
                .orderRequest(orderRequest)
                .build();
    }
}
