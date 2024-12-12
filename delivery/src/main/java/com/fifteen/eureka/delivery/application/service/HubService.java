package com.fifteen.eureka.delivery.application.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateDto;
import com.fifteen.eureka.delivery.application.dto.hub.HubDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubSimpleResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubUpdateDto;
import com.fifteen.eureka.delivery.domain.model.Hub;

public interface HubService {
	Hub createHub(HubCreateDto hubCreateDto);

	PagedModel<HubSimpleResponse> getHubs(Pageable pageable, String search);

	HubDetailsResponse getHub(UUID hubId);

	void updateHub(UUID hubId, HubUpdateDto hubUpdateDto);

	void deleteHub(UUID hubId);
}
