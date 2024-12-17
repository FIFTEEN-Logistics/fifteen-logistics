package com.fifteen.eureka.delivery.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fifteen.eureka.common.auditor.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_delivery SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Delivery extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private UUID orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub startHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub endHub;

	@OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DeliveryManager vendorDeliveryManager;

	@Column(nullable = false)
	private String deliveryAddress;

	@Column(nullable = false)
	private String recipient;

	@Column(nullable = false)
	private String recipientSlackId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

	@Builder
	private Delivery(UUID orderId, Hub startHub, Hub endHub, String deliveryAddress,
		DeliveryManager vendorDeliveryManager, String recipient,
		String recipientSlackId, DeliveryStatus deliveryStatus) {
		this.orderId = orderId;
		this.startHub = startHub;
		this.endHub = endHub;
		this.deliveryAddress = deliveryAddress;
		this.vendorDeliveryManager = vendorDeliveryManager;
		this.recipient = recipient;
		this.recipientSlackId = recipientSlackId;
		this.deliveryStatus = deliveryStatus;
	}

	public void addDeliveryRoute(DeliveryRoute deliveryRoute) {
		this.deliveryRoutes.add(deliveryRoute);
		deliveryRoute.setDelivery(this);
	}

	public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
