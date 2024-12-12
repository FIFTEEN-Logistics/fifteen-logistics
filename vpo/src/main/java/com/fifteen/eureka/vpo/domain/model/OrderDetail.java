package com.fifteen.eureka.vpo.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "p_order_detail")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Where(clause = "is_deleted = false")
public class OrderDetail extends BaseEntity {

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

    public static OrderDetail create(Order order, Product product, int quantity) {
        long productsPrice = (long) product.getProductPrice() * quantity;
        return OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .productsPrice(productsPrice)
                .build();
    }

}
