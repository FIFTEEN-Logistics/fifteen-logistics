package com.fifteen.eureka.ai.presentation.dtos.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AiCreateDeliveryEstimatedTimeResponseDto {

    private String orderRequest;

    public static AiCreateDeliveryEstimatedTimeResponseDto from(String orderRequest) {
        return AiCreateDeliveryEstimatedTimeResponseDto.builder()
                .orderRequest(orderRequest)
                .build();
    }
}
