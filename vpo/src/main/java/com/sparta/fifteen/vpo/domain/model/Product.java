package com.sparta.fifteen.vpo.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_product")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {

    @Id
    @UuidGenerator
    private UUID productId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productPrice;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private boolean isDeleted;

    public static Product create(UUID hubId, String productName, Integer productPrice, Integer quantity, Vendor vendor) {
        return Product.builder()
                .hubId(hubId)
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .vendor(vendor)
                .build();
    }

    public void update(UUID hubId, String productName, Integer productPrice, Integer quantity) {
        this.hubId = hubId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }
}
