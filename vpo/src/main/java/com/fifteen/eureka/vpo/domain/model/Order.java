package com.fifteen.eureka.vpo.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLRestriction(value = "is_deleted = false")
public class Order extends BaseEntity {

    private static final int MAX_NUMBER = 999;
    private static final char MAX_CHAR = 'Z';
    private static int numberSequence = 0;
    private static char charSequence = 'A';

    @Id
    @UuidGenerator
    private UUID orderId;

    @Column(nullable = false)
    private Long userId;

    // nullable = false
    private UUID deliveryId;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String orderRequest;

    @Column(nullable = false)
    private long totalPrice;

    @Column(nullable = false)
    private boolean isCanceled;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Vendor supplier;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Vendor receiver;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

    public static Order create(Long userId, String orderRequest, Vendor receiver, Vendor supplier) {
        return Order.builder()
                .userId(userId)
                .orderRequest(orderRequest)
                .supplier(supplier)
                .receiver(receiver)
                .orderDetails(new ArrayList<>())
                .orderNumber(createOrderNumber())
                .build();
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
    }

    public void addDelivery(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    private static String createOrderNumber() {
        String currentDate = new SimpleDateFormat("yyyyMMddHH").format(new Date());

        if (numberSequence > MAX_NUMBER) {
            numberSequence = 0;
            charSequence++;
            if (charSequence > MAX_CHAR) {
                charSequence = 'A';
            }
        }

        String Suffix = String.format("%c%03d", charSequence, numberSequence);
        numberSequence++;
        return currentDate + Suffix;
    }


    public void updateOrderRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public void calculateTotalPrice() {
        this.totalPrice = orderDetails.stream()
                .mapToLong(OrderDetail::getProductsPrice)
                .sum();
    }

    public void cancel() {
        this.isCanceled = true;
    }

}
