package com.fifteen.eureka.delivery.application.dto.hub;

import java.util.UUID;

import com.fifteen.eureka.delivery.domain.model.Hub;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubCreateDto {
	private UUID centralHubId;
	private Long hubManagerId;
	private String hubName;
	private String hubAddress;
	private double latitude;
	private double longitude;

	public Hub toEntity(Hub centralHub) {
		return Hub.builder()
			.centralHub(centralHub)
			.hubManagerId(hubManagerId)
			.hubName(hubName)
			.hubAddress(hubAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
