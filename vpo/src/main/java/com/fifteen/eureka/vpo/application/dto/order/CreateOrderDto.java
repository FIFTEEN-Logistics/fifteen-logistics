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

    public static CreateOrderDto create(Long userId, UUID supplierId, UUID receiverId, String orderRequest) {
        return CreateOrderDto.builder()
                .userId(userId)
                .supplierId(supplierId)
                .receiverId(receiverId)
                .orderRequest(orderRequest)
                .build();
    }
}
