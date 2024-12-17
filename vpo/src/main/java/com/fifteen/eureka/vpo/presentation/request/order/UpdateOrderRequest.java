package com.fifteen.eureka.vpo.presentation.request.order;

import com.fifteen.eureka.vpo.application.dto.order.UpdateOrderDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

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

    public UpdateOrderDto toDto() {
        return UpdateOrderDto.create(
                this.receiverId,
                this.supplierId,
                this.orderRequest
        );
    }

}
