package com.fifteen.eureka.vpo.presentation.request.order;

import com.fifteen.eureka.vpo.application.dto.order.UpdateOrderDetailDto;
import com.fifteen.eureka.vpo.application.dto.order.UpdateOrderDto;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UpdateOrderRequest {

    private UUID supplierId;
    private UUID receiverId;
    private String orderRequest;
    private List<OrderDetail> orderDetails;


    public UpdateOrderDto toDto() {
        return UpdateOrderDto.create(
                this.receiverId,
                this.supplierId,
                this.orderRequest
        );
    }

    @Getter
    public static class OrderDetail {
        private UUID orderDetailId;
        private UUID productId;
        private int quantity;


        public UpdateOrderDetailDto toDto() {
            return UpdateOrderDetailDto.create(
                    this.orderDetailId,
                    this.productId,
                    this.quantity
            );
        }

    }
}
