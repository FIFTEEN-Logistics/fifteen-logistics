package com.fifteen.eureka.vpo.infrastructure.client.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryDetailsResponse {
    private UUID deliveryId;
    private UUID orderId;
    private String startHubName;
    private String endHubName;
    private String vendorDeliveryManagerName;
    private String deliveryAddress;
    private String recipient;
    private String recipientSlackId;
    private DeliveryStatus deliveryStatus;
    private List<Object> deliveryRoutes;

    public enum DeliveryStatus {
        HUB_WAITING,
        HUB_DELIVERY,
        HUB_ARRIVED,
        DST_DELIVERY,
        DST_ARRIVED
    }
}
