package com.fifteen.eureka.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fifteen.eureka.delivery.domain.model.DeliveryRoute;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
}
