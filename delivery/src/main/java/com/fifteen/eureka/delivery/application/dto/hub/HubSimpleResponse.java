package com.fifteen.eureka.delivery.application.dto.hub;

import java.util.UUID;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HubSimpleResponse {
	private UUID id;
	private String hubName;
	private String hubAddress;
	private Long hubManagerId;

	@Builder
	@QueryProjection
	public HubSimpleResponse(UUID id, String hubName, String hubAddress, Long hubManagerId) {
		this.id = id;
		this.hubName = hubName;
		this.hubAddress = hubAddress;
		this.hubManagerId = hubManagerId;
	}
}
