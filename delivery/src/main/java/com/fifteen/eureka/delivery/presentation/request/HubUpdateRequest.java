package com.fifteen.eureka.delivery.presentation.request;

import java.util.UUID;

import com.fifteen.eureka.delivery.application.dto.HubUpdateDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HubUpdateRequest {
	private UUID centralHubId;
	private Long hubManagerId;
	private String hubName;
	private String hubAddress;
	private double latitude;
	private double longitude;

	public HubUpdateDto toDto() {
		return HubUpdateDto.builder()
			.centralHubId(centralHubId)
			.hubManagerId(hubManagerId)
			.hubName(hubName)
			.hubAddress(hubAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}
}
