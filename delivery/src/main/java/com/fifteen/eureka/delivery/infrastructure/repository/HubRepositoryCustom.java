package com.fifteen.eureka.delivery.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fifteen.eureka.delivery.application.dto.hub.HubSimpleResponse;

public interface HubRepositoryCustom {

	Page<HubSimpleResponse> findAllWithSearch(Pageable pageable, String search);
}
