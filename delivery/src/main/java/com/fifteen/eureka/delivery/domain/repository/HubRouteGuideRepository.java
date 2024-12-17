package com.fifteen.eureka.delivery.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.domain.model.HubRouteGuide;

public interface HubRouteGuideRepository extends JpaRepository<HubRouteGuide, UUID> {
	Optional<HubRouteGuide> findByDepartureHubAndArrivalHub(Hub departureHub, Hub arrivalHub);
}
