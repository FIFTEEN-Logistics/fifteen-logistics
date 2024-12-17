package com.fifteen.eureka.delivery.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.common.auditor.BaseEntity;
import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateResponse;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryRouteDetails;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliverySimpleResponse;
import com.fifteen.eureka.delivery.application.dto.user.UserResponse;
import com.fifteen.eureka.delivery.domain.model.Delivery;
import com.fifteen.eureka.delivery.domain.model.DeliveryManager;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.DeliveryRoute;
import com.fifteen.eureka.delivery.domain.model.DeliveryStatus;
import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.domain.model.HubRouteGuide;
import com.fifteen.eureka.delivery.domain.repository.DeliveryManagerRepository;
import com.fifteen.eureka.delivery.domain.repository.DeliveryRepository;
import com.fifteen.eureka.delivery.domain.repository.DeliveryRouteRepository;
import com.fifteen.eureka.delivery.domain.repository.HubRepository;
import com.fifteen.eureka.delivery.domain.repository.HubRouteGuideRepository;
import com.fifteen.eureka.delivery.infrastructure.client.UserClient;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteRepository deliveryRouteRepository;
	private final HubRepository hubRepository;
	private final HubRouteGuideRepository hubRouteGuideRepository;
	private final DeliveryManagerRepository deliveryManagerRepository;

	private final UserClient userClient;

	private final RedisTemplate<String, Integer> redisTemplate;

	@Override
	@Transactional
	public DeliveryCreateResponse createDelivery(DeliveryCreateRequest deliveryCreateRequest) {
		Hub startHub = hubRepository.findById(deliveryCreateRequest.getStartHubId())
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "출발 허브를 찾을 수 없습니다."));
		Hub endHub = hubRepository.findById(deliveryCreateRequest.getEndHubId())
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "도착 허브를 찾을 수 없습니다."));

		DeliveryManager vendorDeliveryManager = assignDeliveryManager(endHub);

		Delivery delivery = deliveryCreateRequest.toEntity(startHub, endHub, vendorDeliveryManager);

		getDeliveryRouteList(startHub, endHub).forEach(delivery::addDeliveryRoute);

		DeliveryCreateResponse deliveryCreateResponse = DeliveryCreateResponse.from(deliveryRepository.save(delivery));
		UserResponse userResponse = userClient.getUser(vendorDeliveryManager.getId()).getData();
		deliveryCreateResponse.setDeliveryUserName(userResponse.getUsername());
		deliveryCreateResponse.setDeliveryUserEmail(userResponse.getEmail());

		return deliveryCreateResponse;
	}

	@Override
	public Page<DeliverySimpleResponse> getDeliveries(Pageable pageable) {
		Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
		List<DeliverySimpleResponse> deliverySimpleResponses = deliveries.getContent().stream()
			.map(DeliverySimpleResponse::from)
			.toList();
		return new PageImpl<>(deliverySimpleResponses, deliveries.getPageable(), deliveries.getTotalElements());
	}

	@Override
	public DeliveryDetailsResponse getDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findByIdWithDeliveryRoutes(deliveryId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "배송 정보를 찾을 수 없습니다."));

		String vendorDeliveryManagerName = userClient.getUser(delivery.getVendorDeliveryManager().getId())
			.getData()
			.getUsername();

		List<DeliveryRouteDetails> deliveryRouteDetailsList = delivery.getDeliveryRoutes().stream()
			.map((deliveryRoute -> {
				String deliveryManagerName = userClient.getUser(deliveryRoute.getDeliveryManager().getId())
					.getData()
					.getUsername();
				DeliveryRouteDetails deliveryRouteDetails = DeliveryRouteDetails.from(deliveryRoute);
				deliveryRouteDetails.setDeliveryManagerName(deliveryManagerName);
				return deliveryRouteDetails;
			}))
			.toList();

		DeliveryDetailsResponse deliveryDetailsResponse = DeliveryDetailsResponse.from(delivery);
		deliveryDetailsResponse.setVendorDeliveryManagerName(vendorDeliveryManagerName);
		deliveryDetailsResponse.setDeliveryRoutes(deliveryRouteDetailsList);

		return deliveryDetailsResponse;
	}

	@Override
	@Transactional
	public void updateDeliveryStatus(UUID deliveryRouteId, Role role, long userId, DeliveryStatus deliveryStatus) {
		DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "배송 정보를 찾을 수 없습니다."));
		if (role == Role.ROLE_DELIVERY_HUB && deliveryRoute.getDeliveryManager().getId() != userId) {
			throw new CustomApiException(ResErrorCode.FORBIDDEN, "배송 정보를 변경할 권한이 없습니다.");
		}
		if (role == Role.ROLE_ADMIN_HUB && deliveryRoute.getDepartureHub().getHubManagerId() != userId
			&& deliveryRoute.getArrivalHub().getHubManagerId() != userId) {
			throw new CustomApiException(ResErrorCode.FORBIDDEN, "배송 정보를 변경할 권한이 없습니다.");
		}
		deliveryRoute.updateDeliveryStatus(deliveryStatus);
		deliveryRoute.getDelivery().updateDeliveryStatus(deliveryStatus);
	}

	@Override
	@Transactional
	public void deleteDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "Delivery Not Found."));
		if (delivery.getDeliveryStatus() != DeliveryStatus.HUB_WAITING) {
			throw new CustomApiException(ResErrorCode.BAD_REQUEST, "Delivery is already depart");
		}
		delivery.getDeliveryRoutes().forEach(BaseEntity::markAsDeleted);
		delivery.markAsDeleted();
	}

	private List<DeliveryRoute> getDeliveryRouteList(Hub startHub, Hub endHub) {
		List<DeliveryRoute> deliveryRouteList = new ArrayList<>();

		if (startHub.isSameCentralHubWith(endHub)) {
			if (startHub.isCentralHub() || endHub.isCentralHub()) {
				deliveryRouteList.add(createDeliveryRoute(startHub, endHub, 1));
			} else {
				deliveryRouteList.add(createDeliveryRoute(startHub, startHub.getCentralHub(), 1));
				deliveryRouteList.add(createDeliveryRoute(endHub.getCentralHub(), endHub, 2));
			}
		} else {
			if (startHub.isCentralHub() && endHub.isCentralHub()) {
				deliveryRouteList.add(createDeliveryRoute(startHub, endHub, 1));
			} else if (startHub.isCentralHub() && !endHub.isCentralHub()) {
				deliveryRouteList.add(createDeliveryRoute(startHub, endHub.getCentralHub(), 1));
				deliveryRouteList.add(createDeliveryRoute(endHub.getCentralHub(), endHub, 2));
			} else if (!startHub.isCentralHub() && endHub.isCentralHub()) {
				deliveryRouteList.add(createDeliveryRoute(startHub, startHub.getCentralHub(), 1));
				deliveryRouteList.add(createDeliveryRoute(startHub.getCentralHub(), endHub, 2));
			} else {
				deliveryRouteList.add(createDeliveryRoute(startHub, startHub.getCentralHub(), 1));
				deliveryRouteList.add(createDeliveryRoute(startHub.getCentralHub(), endHub.getCentralHub(), 2));
				deliveryRouteList.add(createDeliveryRoute(endHub.getCentralHub(), endHub, 3));
			}
		}

		return deliveryRouteList;
	}

	private DeliveryRoute createDeliveryRoute(Hub departureHub, Hub arrivalHub, int sequence) {
		// 미리 저장된 허브간의 이동 경로 정보 조회
		HubRouteGuide hubRouteGuide = hubRouteGuideRepository.findByDepartureHubAndArrivalHub(departureHub, arrivalHub)
			.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "허브 간 이동경로를 찾을 수 없습니다."));

		// 배송 담당자 배정
		DeliveryManager deliveryManager = assignDeliveryManager(null);

		return DeliveryRoute.builder()
			.departureHub(departureHub)
			.arrivalHub(arrivalHub)
			.expectedDuration(hubRouteGuide.getDuration())
			.expectedDistance(hubRouteGuide.getDistance())
			.routeSequence(sequence)
			.deliveryManager(deliveryManager)
			.deliveryStatus(DeliveryStatus.HUB_WAITING)
			.build();
	}

	private DeliveryManager assignDeliveryManager(Hub hub) {
		DeliveryManager assignedDeliveryManager;
		String key;
		int maxSize;

		if (hub == null) { // 허브 배송 담당자 배정
			maxSize = deliveryManagerRepository.countByDeliveryManagerType(DeliveryManagerType.HUB);

			if (maxSize == 0) {
				throw new CustomApiException(ResErrorCode.NOT_FOUND, "허브 배송 담당자가 없습니다.");
			}

			// 허브 배송 담당자 순번 가져오기
			key = "sequence:hub";
			int currentSequence = getNextSequence(key, maxSize, sequence ->
				deliveryManagerRepository.existsByDeliveryManagerTypeAndDeliverySequence(
					DeliveryManagerType.HUB, sequence));
			assignedDeliveryManager = deliveryManagerRepository.findByDeliveryManagerTypeAndDeliverySequence(
					DeliveryManagerType.HUB, currentSequence)
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 시퀀스의 허브 배송 담당자가 없습니다."));

		} else { // 업체 배송 담당자 배정
			maxSize = deliveryManagerRepository.countByHubAndDeliveryManagerType(hub, DeliveryManagerType.VENDOR);

			if (maxSize == 0) {
				throw new CustomApiException(ResErrorCode.NOT_FOUND, "업체 배송 담당자가 없습니다.");
			}

			// 업체별 배송 담당자 순번 가져오기
			key = "sequence:vendor:" + hub.getId();
			int currentSequence = getNextSequence(key, maxSize, sequence ->
				deliveryManagerRepository.existsByHubAndDeliveryManagerTypeAndDeliverySequence(
					hub, DeliveryManagerType.VENDOR, sequence));
			assignedDeliveryManager = deliveryManagerRepository.findByHubAndDeliveryManagerTypeAndDeliverySequence(
					hub, DeliveryManagerType.VENDOR, currentSequence)
				.orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 시퀀스의 업체 배송 담당자가 없습니다."));
		}

		return assignedDeliveryManager;
	}

	private int getNextSequence(String key, int maxSize, Function<Integer, Boolean> isValid) {
		Integer currentSequence = redisTemplate.opsForValue().get(key);
		if (currentSequence == null) {
			currentSequence = 0;
		}
		int attempts = 0;
		while (attempts < maxSize) {
			if (isValid.apply(currentSequence)) {
				redisTemplate.opsForValue().set(key, (currentSequence + 1) % maxSize);
				return currentSequence;
			}
			currentSequence = (currentSequence + 1) % maxSize;
			attempts++;
		}

		throw new CustomApiException(ResErrorCode.NOT_FOUND, "유효한 배송 담당자를 찾을 수 없습니다.");
	}
}
