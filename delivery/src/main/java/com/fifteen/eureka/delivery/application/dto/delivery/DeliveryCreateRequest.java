package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Delivery;
import com.fifteen.eureka.delivery.domain.model.DeliveryManager;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;
import com.fifteen.eureka.delivery.domain.model.Hub;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryCreateRequest {
	private UUID orderId;
	private UUID startHubId;
	private UUID endHubId;
	private String deliveryAddress;
	private String recipient;
	private String recipientSlackId;

	public Delivery toEntity(Hub startHub, Hub endHub, DeliveryManager vendorDeliveryManager) {
		return Delivery.builder()
			.orderId(orderId)
			.startHub(startHub)
			.endHub(endHub)
			.vendorDeliveryManager(vendorDeliveryManager)
			.deliveryAddress(deliveryAddress)
			.recipient(recipient)
			.recipientSlackId(recipientSlackId)
			.deliveryStatus(DeliveryStatus.HUB_WAITING)
			.build();
	}
}
