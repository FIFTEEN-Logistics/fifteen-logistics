package com.fifteen.eureka.delivery.application.dto.hub;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubUpdateDto {
	private UUID centralHubId;
	private Long hubManagerId;
	private String hubName;
	private String hubAddress;
	private double latitude;
	private double longitude;
}
