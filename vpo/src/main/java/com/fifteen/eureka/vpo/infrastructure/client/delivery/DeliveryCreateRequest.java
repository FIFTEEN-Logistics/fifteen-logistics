package com.fifteen.eureka.vpo.infrastructure.client.delivery;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeliveryCreateRequest {
    private UUID orderId;
    private UUID startHubId;
    private UUID endHubId;
    private String deliveryAddress;
    private String recipient;
    private String recipientSlackId;
}
