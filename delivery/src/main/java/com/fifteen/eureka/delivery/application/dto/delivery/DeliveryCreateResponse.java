package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.List;

import com.fifteen.eureka.delivery.domain.model.Delivery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCreateResponse {
	private String departureHubName;
	private List<String> routingHubNames;
	private String deliveryAddress;
	private String deliveryUserName;
	private String deliveryUserEmail;

	@Builder
	private DeliveryCreateResponse(String departureHubName, List<String> routingHubNames, String deliveryAddress,
		String deliveryUserName, String deliveryUserEmail) {
		this.departureHubName = departureHubName;
		this.routingHubNames = routingHubNames;
		this.deliveryAddress = deliveryAddress;
		this.deliveryUserName = deliveryUserName;
		this.deliveryUserEmail = deliveryUserEmail;
	}

	public static DeliveryCreateResponse from(Delivery delivery) {
		return DeliveryCreateResponse.builder()
			.departureHubName(delivery.getStartHub().getHubName())
			.routingHubNames(delivery.getDeliveryRoutes().stream()
				.map(deliveryRoute -> deliveryRoute.getDepartureHub().getHubName())
				.toList())
			.deliveryAddress(delivery.getDeliveryAddress())
			.build();
	}
}
