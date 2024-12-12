package com.fifteen.eureka.vpo.presentation.request.product;

import com.fifteen.eureka.vpo.application.dto.product.UpdateProductDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateProductRequest {

    private UUID hubId;

    private String productName;

    private int productPrice;

    private int quantity;

    public UpdateProductDto toDto() {
        return UpdateProductDto.create(
                this.hubId,
                this.productName,
                this.productPrice,
                this.quantity);
    }

}
