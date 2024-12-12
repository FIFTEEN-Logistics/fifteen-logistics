package com.fifteen.eureka.delivery.application.service;

import com.fifteen.eureka.delivery.application.dto.deliveryManager.DeliveryManagerCreateDto;
import com.fifteen.eureka.delivery.domain.model.DeliveryManger;

public interface DeliveryManagerService {
	DeliveryManger createDeliveryManager(DeliveryManagerCreateDto deliveryManagerCreateDto);
}
