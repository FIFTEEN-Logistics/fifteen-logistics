package com.fifteen.eureka.delivery.application.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateRequest;
import com.fifteen.eureka.delivery.application.dto.hub.HubDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubSimpleResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubUpdateRequest;
import com.fifteen.eureka.delivery.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.delivery.common.response.ResErrorCode;
import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubServiceImpl implements HubService {

	private final HubRepository hubRepository;

	@Override
	@Transactional
	public Hub createHub(HubCreateRequest hubCreateRequest) {
		Hub centralHub = null;
		if (hubCreateRequest.getCentralHubId() != null) {
			centralHub = hubRepository.findById(hubCreateRequest.getCentralHubId())
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		}
		Hub hub = hubCreateRequest.toEntity(centralHub);
		return hubRepository.save(hub);
	}

	@Override
	public PagedModel<HubSimpleResponse> getHubs(Pageable pageable, String search) {
		return new PagedModel<>(hubRepository.findAllWithSearch(pageable, search));
	}

	@Override
	public HubDetailsResponse getHub(UUID hubId) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		return HubDetailsResponse.from(hub);
	}

	@Override
	@Transactional
	public void updateHub(UUID hubId, HubUpdateRequest hubUpdateRequest) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

		Hub centralHub = hubUpdateRequest.getCentralHubId() != null
			? hubRepository.findById(hubUpdateRequest.getCentralHubId())
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND))
			: null;

		hub.update(
			centralHub,
			hubUpdateRequest.getHubManagerId(),
			hubUpdateRequest.getHubName(),
			hubUpdateRequest.getHubAddress(),
			hubUpdateRequest.getLatitude(),
			hubUpdateRequest.getLongitude()
		);
	}

	@Override
	@Transactional
	public void deleteHub(UUID hubId) {
		hubRepository.deleteById(hubId);
	}

}