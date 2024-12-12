package com.fifteen.eureka.vpo.application.dto.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateProductDto {

    private String productName;

    private int productPrice;

    private int quantity;

    public static UpdateProductDto create(String productName, int productPrice, int quantity) {
        return UpdateProductDto.builder()
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .build();
    }
}
