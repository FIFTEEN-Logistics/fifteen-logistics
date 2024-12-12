package com.fifteen.eureka.delivery.presentation.request;

import java.util.UUID;

import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateDto;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryManagerCreateRequest {
	private Long userId;
	private UUID hubId;
	private DeliveryManagerType deliveryManagerType;

	public DeliveryManagerCreateDto toDto() {
		return DeliveryManagerCreateDto.builder()
			.userId(userId)
			.hubId(hubId)
			.deliveryManagerType(deliveryManagerType)
			.build();
	}
}
