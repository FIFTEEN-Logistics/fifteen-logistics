package com.fifteen.eureka.vpo.presentation.request.order;

import com.fifteen.eureka.vpo.application.dto.order.CreateDeliveryInfoDto;
import com.fifteen.eureka.vpo.application.dto.order.CreateOrderDetailDto;
import com.fifteen.eureka.vpo.application.dto.order.CreateOrderDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateOrderRequest {

    @NotNull(message = "수령 업체 ID는 필수입니다.")
    private UUID receiverId;

    @NotNull(message = "공급 업체 ID는 필수입니다.")
    private UUID supplierId;

    @NotBlank(message = "요청 사항은 필수입니다.")
    @Size(max = 300, message = "요청 사항은 최대 300자까지 입력할 수 있습니다.")
    private String orderRequest;

    @Valid
    @NotEmpty(message = "주문 상세 목록은 최소 1개 이상이어야 합니다.")
    private List<CreateOrderDetailRequest> orderDetails;

    @Valid
    @NotNull(message = "배송 정보는 필수입니다.")
    private CreateDeliveryRequest delivery;

    public CreateOrderDto toDto() {
        return CreateOrderDto.create(
                this.receiverId,
                this.supplierId,
                this.orderRequest
        );
    }

    @Getter
    public static class CreateOrderDetailRequest {

        @NotNull(message = "상품 ID는 필수입니다.")
        private UUID productId;

        @NotNull(message = "수량은 필수입니다.")
        @Positive(message = "수량은 1개 이상이어야 합니다.")
        private int quantity;


        public CreateOrderDetailDto toDto() {
            return CreateOrderDetailDto.create(
                    this.productId,
                    this.quantity
            );
        }

    }

    @Getter
    public static class CreateDeliveryRequest {

        @NotBlank(message = "배송 주소는 필수입니다.")
        @Size(max = 500, message = "배송 주소는 500자까지 입력할 수 있습니다.")
        private String deliveryAddress;

        @NotBlank(message = "수령인 이름은 필수입니다.")
        @Size(max = 50, message = "수령인 이름은 50자까지 입력할 수 있습니다.")
        private String recipient;

        @NotBlank(message = "수령인 Slack ID는 필수입니다.")
        @Size(max = 100, message = "수령인 Slack ID는 100자까지 입력할 수 있습니다.")
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
