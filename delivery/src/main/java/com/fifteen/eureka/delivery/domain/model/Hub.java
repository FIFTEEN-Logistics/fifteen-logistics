package com.fifteen.eureka.delivery.domain.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import com.fifteen.eureka.common.auditor.BaseEntity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
public class Hub extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Hub centralHub;

	@Column(nullable = false)
	private Long hubManagerId;

	@Column(nullable = false)
	private String hubName;

	@Column(nullable = false)
	private String hubAddress;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;

	@Builder
	public Hub(Hub centralHub, Long hubManagerId, String hubName, String hubAddress, double latitude, double longitude) {
		this.centralHub = centralHub != null ? centralHub : this;
		this.hubManagerId = hubManagerId;
		this.hubName = hubName;
		this.hubAddress = hubAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public boolean isCentralHub() {
		return centralHub.getId() == id;
	}

	public void update(Hub centralHub, Long hubManagerId, String hubName, String hubAddress, double latitude, double longitude) {
		this.centralHub = centralHub != null ? centralHub : this;
		this.hubManagerId = hubManagerId;
		this.hubName = hubName;
		this.hubAddress = hubAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public boolean isSameCentralHubWith(Hub hub) {
		return this.centralHub.getId() == hub.getCentralHub().getId();
	}
}
