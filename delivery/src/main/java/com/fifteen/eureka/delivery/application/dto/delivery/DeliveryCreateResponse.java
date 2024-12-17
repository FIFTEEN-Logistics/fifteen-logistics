package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.List;
import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Delivery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCreateResponse {
	private UUID deliveryId;
	private String departureHubName;
	private List<String> routingHubNames;
	private String deliveryAddress;
	private String deliveryUserName;
	private String deliveryUserEmail;

	@Builder
	private DeliveryCreateResponse(UUID deliveryId, String departureHubName, List<String> routingHubNames, String deliveryAddress,
		String deliveryUserName, String deliveryUserEmail) {
		this.deliveryId = deliveryId;
		this.departureHubName = departureHubName;
		this.routingHubNames = routingHubNames;
		this.deliveryAddress = deliveryAddress;
		this.deliveryUserName = deliveryUserName;
		this.deliveryUserEmail = deliveryUserEmail;
	}

	public static DeliveryCreateResponse from(Delivery delivery) {
		return DeliveryCreateResponse.builder()
			.deliveryId(delivery.getId())
			.departureHubName(delivery.getStartHub().getHubName())
			.routingHubNames(delivery.getDeliveryRoutes().stream()
				.map(deliveryRoute -> deliveryRoute.getArrivalHub().getHubName())
				.toList())
			.deliveryAddress(delivery.getDeliveryAddress())
			.build();
	}
}
