package com.fifteen.eureka.vpo.infrastructure.client.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryCreateResponse {
    private UUID deliveryId;
    private String departureHubName;
    private List<String> routingHubNames;
    private String deliveryAddress;
    private String deliveryUserName;
    private String deliveryUserEmail;
}
