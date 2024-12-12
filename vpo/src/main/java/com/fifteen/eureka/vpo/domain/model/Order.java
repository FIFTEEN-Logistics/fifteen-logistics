package com.fifteen.eureka.vpo.domain.model;

import com.fifteen.eureka.common.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE p_order SET is_deleted = true WHERE order_id = ?")
@Where(clause = "is_deleted = false")
public class Order extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID orderId;

    @Column(nullable = false)
    private Long userId;

    // nullable = false
    private UUID deliveryId;

    @Column(nullable = false)
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

    public static Order create(Long userId, String orderRequest, Vendor supplier, Vendor receiver) {
        return Order.builder()
                .userId(userId)
                .orderRequest(orderRequest)
                .supplier(supplier)
                .receiver(receiver)
                .orderDetails(new ArrayList<>())
                .build();
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
    }

    public void addDelivery(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void addOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

//    public void updateSupplier(Vendor newSupplier) {
//        this.supplier = newSupplier;
//    }

    public void updateReceiver(Vendor newReceiver) {
        this.receiver = newReceiver;
    }

    public void updateOrderRequest(String newRequest) {
        this.orderRequest = newRequest;
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
