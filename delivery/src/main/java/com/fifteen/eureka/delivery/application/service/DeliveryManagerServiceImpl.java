package com.fifteen.eureka.delivery.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerUpdateRequest;
import com.fifteen.eureka.delivery.domain.model.DeliveryManager;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
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

	//TODO: 허브 관리자일 경우 본인 허브에 소속된 배송 담당자만 관리 제한 추가
	//TODO: 배송 담당자 본인은 자신의 정보만 확인 가능

	@Override
	@Transactional
	public DeliveryManager createDeliveryManager(DeliveryManagerCreateRequest deliveryManagerCreateRequest) {
		Hub hub = null;
		if (deliveryManagerCreateRequest.getHubId() != null) {
			hub = hubRepository.findById(deliveryManagerCreateRequest.getHubId())
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		}

		// 최대 시퀀스 조회 후 그 다음 시퀀스로 지정
		int nextSequence = getMaxSequence(deliveryManagerCreateRequest.getDeliveryManagerType(), deliveryManagerCreateRequest.getHubId()) + 1;

		DeliveryManager deliveryManager = deliveryManagerCreateRequest.toEntity(hub, nextSequence);

		return deliveryManagerRepository.save(deliveryManager);
	}

	@Override
	@Transactional
	public void updateDeliveryManager(Long userId, DeliveryManagerUpdateRequest deliveryManagerUpdateRequest) {
		DeliveryManager deliveryManager = deliveryManagerRepository.findById(userId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

		Hub hub = null;
		if (deliveryManagerUpdateRequest.getHubId() != null) {
			hub = hubRepository.findById(deliveryManagerUpdateRequest.getHubId())
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
		}

		deliveryManager.update(
			hub,
			deliveryManagerUpdateRequest.getDeliveryManagerType(),
			getMaxSequence(deliveryManagerUpdateRequest.getDeliveryManagerType(), deliveryManagerUpdateRequest.getHubId()) + 1
		);
	}

	@Override
	@Transactional
	public void deleteDeliveryManager(Long userId) {
		deliveryManagerRepository.findById(userId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "Delivery manager not found"))
			.markAsDeleted();
	}

	// 배송 담당자 타입 별로 현재 최대 배송 순서 조회
	private int getMaxSequence(DeliveryManagerType deliveryManagerType, UUID hubId) {
		int sequence;
		if (deliveryManagerType == DeliveryManagerType.HUB) {
			sequence = deliveryManagerRepository.findMaxSequenceForHubDeliveryManagers(DeliveryManagerType.HUB);
		} else {
			sequence = deliveryManagerRepository.findMaxSequenceForVendorDeliveryManagers(hubId, DeliveryManagerType.VENDOR);
		}
		return sequence;
	}
}
