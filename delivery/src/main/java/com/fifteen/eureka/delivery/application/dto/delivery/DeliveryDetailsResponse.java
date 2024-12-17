package com.fifteen.eureka.delivery.application.dto.delivery;

import java.util.List;
import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Delivery;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDetailsResponse {

	private UUID deliveryId;
	private UUID orderId;
	private String startHubName;
	private String endHubName;
	private String vendorDeliveryManagerName;
	private String deliveryAddress;
	private String recipient;
	private String recipientSlackId;
	private DeliveryStatus deliveryStatus;
	private List<DeliveryRouteDetails> deliveryRoutes;

	@Builder
	private DeliveryDetailsResponse(UUID deliveryId, UUID orderId, String startHubName, String endHubName,
		String vendorDeliveryManagerName, String deliveryAddress, String recipient, String recipientSlackId,
		DeliveryStatus deliveryStatus, List<DeliveryRouteDetails> deliveryRoutes) {
		this.deliveryId = deliveryId;
		this.orderId = orderId;
		this.startHubName = startHubName;
		this.endHubName = endHubName;
		this.vendorDeliveryManagerName = vendorDeliveryManagerName;
		this.deliveryAddress = deliveryAddress;
		this.recipient = recipient;
		this.recipientSlackId = recipientSlackId;
		this.deliveryStatus = deliveryStatus;
		this.deliveryRoutes = deliveryRoutes;
	}

	public static DeliveryDetailsResponse from(Delivery delivery) {
		return DeliveryDetailsResponse.builder()
			.deliveryId(delivery.getId())
			.orderId(delivery.getOrderId())
			.startHubName(delivery.getStartHub().getHubName())
			.endHubName(delivery.getEndHub().getHubName())
			.deliveryAddress(delivery.getDeliveryAddress())
			.recipient(delivery.getRecipient())
			.recipientSlackId(delivery.getRecipientSlackId())
			.deliveryStatus(delivery.getDeliveryStatus())
			.build();
	}
}
