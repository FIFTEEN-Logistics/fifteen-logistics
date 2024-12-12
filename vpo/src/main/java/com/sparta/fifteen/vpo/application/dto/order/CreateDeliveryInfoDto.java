package com.sparta.fifteen.vpo.application.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateDeliveryInfoDto {
    private String deliveryAddress;
    private String recipient;
    private String recipientSlackId;

    public static CreateDeliveryInfoDto create(String deliveryAddress, String recipient, String recipientSlackId) {
        return CreateDeliveryInfoDto.builder()
                .deliveryAddress(deliveryAddress)
                .recipient(recipient)
                .recipientSlackId(recipientSlackId)
                .build();
    }
}
