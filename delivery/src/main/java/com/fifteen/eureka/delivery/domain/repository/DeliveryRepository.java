package com.fifteen.eureka.delivery.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fifteen.eureka.delivery.domain.model.Delivery;
import com.fifteen.eureka.delivery.infrastructure.repository.DeliveryRepositoryCustom;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>, DeliveryRepositoryCustom {

	@Query("select d from Delivery d join fetch d.deliveryRoutes dr where d.id = :deliveryId order by dr.routeSequence asc")
	Optional<Delivery> findByIdWithDeliveryRoutes(@Param("deliveryId") UUID deliveryId);
}
