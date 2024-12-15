package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Delivery;
import com.fifteen.eureka.delivery.domain.model.DeliveryRoute;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeliverySimpleResponse {
	private UUID id;
	private UUID orderId;
	private DeliveryStatus deliveryStatus;
	private String startHubName;
	private String endHubName;
	private int expectedDuration;
	private int expectedDistance;
	private int actualDuration;
	private int actualDistance;

	@Builder
	private DeliverySimpleResponse(UUID id, UUID orderId, DeliveryStatus deliveryStatus, String startHubName,
		String endHubName, int expectedDuration, int expectedDistance, int actualDuration, int actualDistance) {
		this.id = id;
		this.orderId = orderId;
		this.deliveryStatus = deliveryStatus;
		this.startHubName = startHubName;
		this.endHubName = endHubName;
		this.expectedDuration = expectedDuration;
		this.expectedDistance = expectedDistance;
		this.actualDuration = actualDuration;
		this.actualDistance = actualDistance;
	}

	public static DeliverySimpleResponse from(Delivery delivery) {
		return DeliverySimpleResponse.builder()
			.id(delivery.getId())
			.orderId(delivery.getOrderId())
			.deliveryStatus(delivery.getDeliveryStatus())
			.startHubName(delivery.getStartHub().getHubName())
			.endHubName(delivery.getEndHub().getHubName())
			.expectedDuration(delivery.getDeliveryRoutes().stream()
				.mapToInt(DeliveryRoute::getExpectedDuration)
				.sum())
			.expectedDistance(delivery.getDeliveryRoutes().stream()
				.mapToInt(DeliveryRoute::getExpectedDistance)
				.sum())
			.actualDuration(
				delivery.getDeliveryStatus() == DeliveryStatus.DST_ARRIVED ? delivery.getDeliveryRoutes().stream()
					.mapToInt(DeliveryRoute::getActualDuration)
					.sum() : 0)
			.actualDistance(
				delivery.getDeliveryStatus() == DeliveryStatus.DST_ARRIVED ? delivery.getDeliveryRoutes().stream()
					.mapToInt(DeliveryRoute::getActualDistance)
					.sum() : 0)
			.build();
	}
}
