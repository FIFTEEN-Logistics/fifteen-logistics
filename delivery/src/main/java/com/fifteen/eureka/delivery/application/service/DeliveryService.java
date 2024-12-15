package com.fifteen.eureka.delivery.application.service;

import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.delivery.domain.model.Delivery;

public interface DeliveryService {
	Delivery createDelivery(DeliveryCreateRequest deliveryCreateRequest);
}
