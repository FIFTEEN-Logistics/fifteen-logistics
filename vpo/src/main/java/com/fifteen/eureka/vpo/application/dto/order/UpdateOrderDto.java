package com.fifteen.eureka.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateOrderDto {

    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;

    public static UpdateOrderDto create(UUID supplierId, UUID receiverId, String orderRequest) {
        return UpdateOrderDto.builder()
                .supplierId(supplierId)
                .receiverId(receiverId)
                .orderRequest(orderRequest)
                .build();
    }
}
