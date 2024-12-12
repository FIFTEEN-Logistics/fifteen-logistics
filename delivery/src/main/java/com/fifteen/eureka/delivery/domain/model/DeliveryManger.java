package com.fifteen.eureka.delivery.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManger {

	@Id
	@Column(name = "user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Hub hub;

	@Column(nullable = false)
	private int deliverySequence;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeliveryManagerType deliveryManagerType;

	@Builder
	private DeliveryManger(Long id, Hub hub, int deliverySequence, DeliveryManagerType deliveryManagerType) {
		this.id = id;
		this.hub = hub;
		this.deliverySequence = deliverySequence;
		this.deliveryManagerType = deliveryManagerType;
	}
}
