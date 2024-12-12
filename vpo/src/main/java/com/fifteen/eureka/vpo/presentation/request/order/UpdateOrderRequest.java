package com.fifteen.eureka.vpo.presentation.request.order;

import com.fifteen.eureka.vpo.application.dto.order.UpdateOrderDetailDto;
import com.fifteen.eureka.vpo.application.dto.order.UpdateOrderDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UpdateOrderRequest {

    @NotNull(message = "공급 업체 ID는 필수입니다.")
    private UUID supplierId;

    @NotNull(message = "수령 업체 ID는 필수입니다.")
    private UUID receiverId;

    @NotBlank(message = "요청 사항은 필수입니다.")
    @Size(max = 300, message = "요청 사항은 최대 300자까지 입력할 수 있습니다.")
    private String orderRequest;

    @Valid
    @NotEmpty(message = "주문 상세 목록은 최소 1개 이상이어야 합니다.")
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

        @NotNull(message = "주문 상세 ID는 필수입니다.")
        private UUID orderDetailId;

        @NotNull(message = "상품 ID는 필수입니다.")
        private UUID productId;

        @NotNull(message = "수량은 필수입니다.")
        @Positive(message = "수량은 1개 이상이어야 합니다.")
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
