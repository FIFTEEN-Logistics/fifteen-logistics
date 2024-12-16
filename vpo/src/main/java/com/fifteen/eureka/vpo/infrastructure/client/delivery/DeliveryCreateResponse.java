package com.fifteen.eureka.vpo.infrastructure.client.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryCreateResponse {
    private String departureHubName;
    private List<String> routingHubNames;
    private String deliveryAddress;
    private String deliveryUserName;
    private String deliveryUserEmail;
}
