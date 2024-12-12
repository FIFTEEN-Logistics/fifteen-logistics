package com.fifteen.eureka.delivery.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRoute {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub departureHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub arrivalHub;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DeliveryManger deliveryManger;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Delivery delivery;

	@Column(nullable = false)
	private int routeSequence;

	@Column(nullable = false)
	private int expectedDistance;

	@Column(nullable = false)
	private int expectedDuration;

	private int actualDistance;

	private int actualDuration;

	@Column(nullable = false)
	private DeliveryStatus deliveryStatus;
}
