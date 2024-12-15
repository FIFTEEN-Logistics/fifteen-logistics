package com.fifteen.eureka.delivery.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.delivery.application.dto.delivery.DeliverySimpleResponse;
import com.fifteen.eureka.delivery.domain.model.Delivery;

public interface DeliveryService {
	Delivery createDelivery(DeliveryCreateRequest deliveryCreateRequest);

	DeliveryDetailsResponse getDelivery(UUID deliveryId);

	Page<DeliverySimpleResponse> getDeliveries(Pageable pageable);
}
