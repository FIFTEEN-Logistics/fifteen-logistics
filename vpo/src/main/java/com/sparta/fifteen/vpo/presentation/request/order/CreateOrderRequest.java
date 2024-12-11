package com.sparta.fifteen.vpo.presentation.request.order;


import com.sparta.fifteen.vpo.application.dto.order.CreateDeliveryInfoDto;
import com.sparta.fifteen.vpo.application.dto.order.CreateOrderDetailDto;
import com.sparta.fifteen.vpo.application.dto.order.CreateOrderDto;
import com.sparta.fifteen.vpo.domain.model.OrderDetail;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateOrderRequest {

    private Long userId;
    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;
    private List<OrderDetail> orderDetails;
    private DeliveryInfo delivery;

    public CreateOrderDto toDto() {
        return CreateOrderDto.create(
                this.userId,
                this.receiverId,
                this.supplierId,
                this.orderRequest
        );
    }

    @Getter
    public static class OrderDetail {
        private UUID productId;
        private int quantity;


        public CreateOrderDetailDto toDto() {
            return CreateOrderDetailDto.create(
                    this.productId,
                    this.quantity
            );
        }

    }

    @Getter
    public static class DeliveryInfo {
        private String deliveryAddress;
        private String recipient;
        private String recipientSlackId;

        public CreateDeliveryInfoDto toDto() {
            return CreateDeliveryInfoDto.create(
                    this.deliveryAddress,
                    this.recipient,
                    this.recipientSlackId
            );
        }
    }

}
