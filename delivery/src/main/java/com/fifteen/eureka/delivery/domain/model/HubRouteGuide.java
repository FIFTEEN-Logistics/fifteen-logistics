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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hub_route_guide")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRouteGuide {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub departureHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub arrivalHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DeliveryManger deliveryManger;

	@Column(nullable = false)
	private int duration;

	@Column(nullable = false)
	private int distance;
}
