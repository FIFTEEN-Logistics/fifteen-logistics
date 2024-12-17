package com.fifteen.eureka.delivery.application.dto.deliveryManager;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;

import lombok.Getter;

@Getter
public class DeliveryManagerUpdateRequest {
	private UUID hubId;
	private DeliveryManagerType deliveryManagerType;
}
