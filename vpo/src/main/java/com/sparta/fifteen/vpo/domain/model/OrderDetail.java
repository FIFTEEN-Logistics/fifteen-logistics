package com.sparta.fifteen.vpo.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_order_detail")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDetail {

    @UuidGenerator
    @Id
    private String orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long productsPrice;

    @Column(nullable = false)
    private boolean isDeleted;

    public static OrderDetail create(Order order, Product product, int quantity, long productsPrice) {
        return OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .productsPrice(productsPrice)
                .build();
    }
}
