package com.fifteen.eureka.delivery.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManger {

	@Id
	private Long deliveryManagerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Hub affiliatedHub;

	private int deliverySequence;

	private ManagerType managerType;
}
