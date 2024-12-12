package com.fifteen.eureka.delivery.application.dto.deliveryManager;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.DeliveryManger;
import com.fifteen.eureka.delivery.domain.model.Hub;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerCreateRequest {
	private Long userId;
	private UUID hubId;
	private DeliveryManagerType deliveryManagerType;

	public DeliveryManger toEntity(Hub hub, int sequence) {
		return DeliveryManger.builder()
			.id(userId)
			.hub(hub)
			.deliverySequence(sequence)
			.deliveryManagerType(deliveryManagerType)
			.build();
	}
}
