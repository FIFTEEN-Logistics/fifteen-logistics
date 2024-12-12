package com.fifteen.eureka.delivery.presentation.request;

import java.util.UUID;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HubCreateRequest {
	private UUID centralHubId;
	private Long hubManagerId;
	private String hubName;
	private String hubAddress;
	private double latitude;
	private double longitude;

	public HubCreateDto toDto() {
		return HubCreateDto.builder()
			.centralHubId(centralHubId)
			.hubManagerId(hubManagerId)
			.hubName(hubName)
			.hubAddress(hubAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
