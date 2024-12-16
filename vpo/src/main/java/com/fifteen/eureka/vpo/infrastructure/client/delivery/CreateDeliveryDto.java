package com.fifteen.eureka.vpo.infrastructure.client.delivery;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CreateDeliveryDto {
    private UUID orderId;
    private String deliveryAddress;
    private String recipient;
    private String recipientSlackId;
}
