package com.fifteen.eureka.delivery.application.dto.deliveryManager;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.DeliveryManager;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.Hub;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerCreateRequest {
	private Long userId;
	private UUID hubId;
	private DeliveryManagerType deliveryManagerType;

	public DeliveryManager toEntity(Hub hub, int sequence) {
		return DeliveryManager.builder()
			.id(userId)
			.hub(hub)
			.deliverySequence(sequence)
			.deliveryManagerType(deliveryManagerType)
			.build();
	}
}
