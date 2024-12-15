package com.fifteen.eureka.delivery.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fifteen.eureka.delivery.application.dto.delivery.DeliverySimpleResponse;
import com.fifteen.eureka.delivery.common.role.Role;

public interface DeliveryRepositoryCustom {
	Page<DeliverySimpleResponse> findAllByRole(Pageable pageable, Role role, Long userId);
}
