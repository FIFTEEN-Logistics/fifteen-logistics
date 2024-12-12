package com.fifteen.eureka.delivery.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.delivery.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.delivery.common.response.ResErrorCode;
import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.DeliveryManger;
import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.domain.repository.DeliveryManagerRepository;
import com.fifteen.eureka.delivery.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

	private final HubRepository hubRepository;
	private final DeliveryManagerRepository deliveryManagerRepository;

	@Override
	@Transactional
	public DeliveryManger createDeliveryManager(DeliveryManagerCreateRequest deliveryManagerCreateRequest) {
		Hub hub = null;
		if (deliveryManagerCreateRequest.getHubId() != null) {
			hub = hubRepository.findById(deliveryManagerCreateRequest.getHubId())
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		}

		int nextSequence = getMaxSequence(deliveryManagerCreateRequest) + 1;

		DeliveryManger deliveryManger = deliveryManagerCreateRequest.toEntity(hub, nextSequence);

		return deliveryManagerRepository.save(deliveryManger);
	}

	private int getMaxSequence(DeliveryManagerCreateRequest deliveryManagerCreateRequest) {
		int sequence;
		if (deliveryManagerCreateRequest.getDeliveryManagerType() == DeliveryManagerType.HUB) {
			sequence = deliveryManagerRepository.findMaxSequenceForHubDeliveryManagers(DeliveryManagerType.HUB);
		} else {
			sequence = deliveryManagerRepository.findMaxSequenceForVendorDeliveryManagers(deliveryManagerCreateRequest.getHubId(), DeliveryManagerType.VENDOR);
		}
		return sequence;
	}
}
