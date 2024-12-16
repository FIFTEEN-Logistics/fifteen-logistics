package com.fifteen.eureka.delivery.application.dto;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Hub;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HubDetailsResponse {
	private UUID id;
	private String centralHub;
	private Long hubManagerId;
	private String hubName;
	private String hubAddress;
	private double latitude;
	private double longitude;

	@Builder
	private HubDetailsResponse(UUID id, String centralHub, Long hubManagerId, String hubName, String hubAddress,
		double latitude, double longitude) {
		this.id = id;
		this.centralHub = centralHub;
		this.hubManagerId = hubManagerId;
		this.hubName = hubName;
		this.hubAddress = hubAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static HubDetailsResponse from(Hub hub) {
		return HubDetailsResponse.builder()
			.id(hub.getId())
			.centralHub(hub.isCentralHub() ? null : hub.getCentralHub().getHubName())
			.hubManagerId(hub.getHubManagerId())
			.hubName(hub.getHubName())
			.hubAddress(hub.getHubAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.build();
	}
}
