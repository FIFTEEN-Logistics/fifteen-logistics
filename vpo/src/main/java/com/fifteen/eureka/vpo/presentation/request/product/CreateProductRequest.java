package com.fifteen.eureka.vpo.presentation.request.product;

import com.fifteen.eureka.vpo.application.dto.product.CreateProductDto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateProductRequest {

    private UUID vendorId;

    private UUID hubId;

    private String productName;

    private int productPrice;

    private int quantity;

    public CreateProductDto toDto() {
        return CreateProductDto.create(this.vendorId, this.hubId, this.productName, this.productPrice, this.quantity);
    }


}
