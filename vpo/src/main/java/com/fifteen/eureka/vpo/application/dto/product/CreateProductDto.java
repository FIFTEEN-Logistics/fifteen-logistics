package com.fifteen.eureka.vpo.application.dto.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateProductDto {

    private UUID vendorId;

    private String productName;

    private int productPrice;

    private int quantity;

    public static CreateProductDto create(
            UUID vendorId,String productName,int productPrice,int quantity) {

        return CreateProductDto.builder()
                .vendorId(vendorId)
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .build();
    }
}
