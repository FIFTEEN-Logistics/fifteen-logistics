package com.fifteen.eureka.vpo.infrastructure.client;


import lombok.Builder;

import java.util.UUID;

@Builder
public class CreateDeliveryDto {
    private UUID orderId;
    private String deliveryAddress;
    private String recipient;
    private String recipientSlackId;
}
