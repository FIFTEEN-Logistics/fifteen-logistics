package com.fifteen.eureka.vpo.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_vendor")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLRestriction(value = "is_deleted = false")
public class Vendor extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID vendorId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private Long hubManagerId;

    @Column(nullable = false)
    private String vendorName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VendorType vendorType;

    @Column(nullable = false)
    private String vendorAddress;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<Product>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> suppliedOrders = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> receivedOrders = new ArrayList<>();

    public static Vendor create(UUID hubId, Long userId, Long hubManagerId, String vendorName, VendorType vendorType, String vendorAddress) {
        return Vendor.builder()
                .hubId(hubId)
                .userId(userId)
                .hubManagerId(hubManagerId)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .vendorAddress(vendorAddress)
                .build();
    }

    public void update(Long userId, String vendorName, VendorType vendorType, String vendorAddress) {
        this.userId = userId;
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
    }

    public void hunInfoUpdate(UUID hubId, Long hubManagerId) {
        this.hubId = hubId;
        this.hubManagerId = hubManagerId;
    }

}
