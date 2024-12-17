package com.fifteen.eureka.delivery.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fifteen.eureka.delivery.domain.model.DeliveryManager;
import com.fifteen.eureka.delivery.domain.model.DeliveryManagerType;
import com.fifteen.eureka.delivery.domain.model.Hub;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {

	@Query("select coalesce(max(dm.deliverySequence), 0) from DeliveryManager dm where dm.deliveryManagerType = :type")
	int findMaxSequenceForHubDeliveryManagers(@Param("type") DeliveryManagerType type);

	@Query("select coalesce(max(dm.deliverySequence), 0) from DeliveryManager dm where dm.hub.id = :hubId and dm.deliveryManagerType = :type")
	int findMaxSequenceForVendorDeliveryManagers(@Param("hubId") UUID hubId, @Param("type") DeliveryManagerType type);

	int countByDeliveryManagerType(DeliveryManagerType deliveryManagerType);

	int countByHubAndDeliveryManagerType(Hub hub, DeliveryManagerType deliveryManagerType);

	Optional<DeliveryManager> findByDeliveryManagerTypeAndDeliverySequence(DeliveryManagerType deliveryManagerType, int deliverySequence);

	Optional<DeliveryManager> findByHubAndDeliveryManagerTypeAndDeliverySequence(Hub hub, DeliveryManagerType deliveryManagerType, int deliverySequence);

	Boolean existsByDeliveryManagerTypeAndDeliverySequence(DeliveryManagerType deliveryManagerType, int deliverySequence);

	Boolean existsByHubAndDeliveryManagerTypeAndDeliverySequence(Hub hub, DeliveryManagerType deliveryManagerType, int deliverySequence);
}
