package com.sparta.fifteen.vpo.domain.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Vendor {

    @Id
    @UuidGenerator
    private UUID vendorId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private UUID hubId;

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

//    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Order> orders = new ArrayList<Order>();

    public static Vendor create(UUID hubId, Long userId, String vendorName, VendorType vendorType, String vendorAddress) {
        return Vendor.builder()
                .hubId(hubId)
                .userId(userId)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .vendorAddress(vendorAddress)
                .build();
    }

    public void update(UUID hubId, Long userId, String vendorName, VendorType vendorType, String vendorAddress) {
        this.hubId = hubId;
        this.userId = userId;
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
