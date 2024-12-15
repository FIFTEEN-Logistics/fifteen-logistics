package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.DeliveryRoute;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeliveryRouteDetails {
	private UUID id;
	private String departureHubName;
	private String arrivalHubName;
	private String deliveryManagerName;
	private int routeSequence;
	private int expectedDistance;
	private int expectedDuration;
	private int actualDistance;
	private int actualDuration;
	private DeliveryStatus deliveryStatus;

	public static DeliveryRouteDetails from(DeliveryRoute deliveryRoute) {
		return DeliveryRouteDetails.builder()
			.id(deliveryRoute.getId())
			.departureHubName(deliveryRoute.getDepartureHub().getHubName())
			.arrivalHubName(deliveryRoute.getArrivalHub().getHubName())
			.routeSequence(deliveryRoute.getRouteSequence())
			.expectedDistance(deliveryRoute.getExpectedDistance())
			.expectedDuration(deliveryRoute.getExpectedDuration())
			.actualDistance(deliveryRoute.getActualDistance())
			.actualDuration(deliveryRoute.getActualDuration())
			.deliveryStatus(deliveryRoute.getDeliveryStatus())
			.build();
	}

}
