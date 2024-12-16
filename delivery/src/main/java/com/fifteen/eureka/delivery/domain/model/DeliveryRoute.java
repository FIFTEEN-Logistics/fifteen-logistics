package com.fifteen.eureka.delivery.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fifteen.eureka.delivery.common.auditor.BaseEntity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_delivery_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_delivery_route SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class DeliveryRoute extends BaseEntity {

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
	private DeliveryManager deliveryManager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@Setter
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

	@Builder
	private DeliveryRoute(UUID id, Hub departureHub, Hub arrivalHub, DeliveryManager deliveryManager, Delivery delivery,
		int routeSequence, int expectedDistance, int expectedDuration, int actualDistance, int actualDuration,
		DeliveryStatus deliveryStatus) {
		this.id = id;
		this.departureHub = departureHub;
		this.arrivalHub = arrivalHub;
		this.deliveryManager = deliveryManager;
		this.delivery = delivery;
		this.routeSequence = routeSequence;
		this.expectedDistance = expectedDistance;
		this.expectedDuration = expectedDuration;
		this.actualDistance = actualDistance;
		this.actualDuration = actualDuration;
		this.deliveryStatus = deliveryStatus;
	}

	public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
