package com.fifteen.eureka.delivery.application.service;

import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateRequest;
import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerUpdateRequest;
import com.fifteen.eureka.delivery.domain.model.DeliveryManger;

public interface DeliveryManagerService {
	DeliveryManger createDeliveryManager(DeliveryManagerCreateRequest deliveryManagerCreateRequest);

	void updateDeliveryManager(Long userId, DeliveryManagerUpdateRequest deliveryManagerUpdateRequest);

	void deleteDeliveryManager(Long userId);
}
