package com.fifteen.eureka.vpo.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
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
public class Product extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID productId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    public static Product create(UUID hubId, String productName, int productPrice, int quantity, Vendor vendor) {
        return Product.builder()
                .hubId(hubId)
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .vendor(vendor)
                .build();
    }

    public void update(String productName, int productPrice, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }


    public void updateQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
