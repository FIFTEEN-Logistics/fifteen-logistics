package com.fifteen.eureka.delivery.application.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateDto;
import com.fifteen.eureka.delivery.application.dto.hub.HubDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubSimpleResponse;
import com.fifteen.eureka.delivery.application.dto.hub.HubUpdateDto;
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
	public Hub createHub(HubCreateDto hubCreateDto) {
		Hub centralHub = null;
		if (hubCreateDto.getCentralHubId() != null) {
			centralHub = hubRepository.findById(hubCreateDto.getCentralHubId())
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		}
		Hub hub = hubCreateDto.toEntity(centralHub);
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
	public void updateHub(UUID hubId, HubUpdateDto hubUpdateDto) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

		Hub centralHub = hubUpdateDto.getCentralHubId() != null
			? hubRepository.findById(hubUpdateDto.getCentralHubId())
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND))
			: null;

		hub.update(
			centralHub,
			hubUpdateDto.getHubManagerId(),
			hubUpdateDto.getHubName(),
			hubUpdateDto.getHubAddress(),
			hubUpdateDto.getLatitude(),
			hubUpdateDto.getLongitude()
		);
	}

	@Override
	@Transactional
	public void deleteHub(UUID hubId) {
		hubRepository.deleteById(hubId);
	}

}