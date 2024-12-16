package com.fifteen.eureka.delivery.domain.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fifteen.eureka.common.auditor.BaseEntity;

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
@SQLDelete(sql = "UPDATE p_delivery_manager SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class DeliveryManager extends BaseEntity {

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
	private DeliveryManager(Long id, Hub hub, int deliverySequence, DeliveryManagerType deliveryManagerType) {
		this.id = id;
		this.hub = hub;
		this.deliverySequence = deliverySequence;
		this.deliveryManagerType = deliveryManagerType;
	}

	public void update(Hub hub, DeliveryManagerType deliveryManagerType, int deliverySequence) {
		this.hub = hub;
		this.deliveryManagerType = deliveryManagerType;
		this.deliverySequence = deliverySequence;
	}
}
