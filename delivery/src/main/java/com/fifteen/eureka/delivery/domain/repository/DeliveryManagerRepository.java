package com.fifteen.eureka.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.DeliveryManger;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManger, Long> {

	@Query("select coalesce(max(dm.deliverySequence), 0) from DeliveryManger dm where dm.deliveryManagerType = :type")
	int findMaxSequenceForHubDeliveryManagers(@Param("type") DeliveryManagerType type);

	@Query("select coalesce(max(dm.deliverySequence), 0) from DeliveryManger dm where dm.hub.id = :hubId and dm.deliveryManagerType = :type")
	int findMaxSequenceForVendorDeliveryManagers(@Param("hubId") UUID hubId, @Param("type") DeliveryManagerType type);
}
