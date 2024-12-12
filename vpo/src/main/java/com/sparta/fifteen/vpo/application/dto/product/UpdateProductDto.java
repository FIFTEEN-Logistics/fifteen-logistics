package com.sparta.fifteen.vpo.application.dto.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateProductDto {

    private UUID hubId;

    private String productName;

    private int productPrice;

    private int quantity;

    public static UpdateProductDto create(
            UUID hubId, String productName, int productPrice, int quantity) {
        return UpdateProductDto.builder()
                .hubId(hubId)
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .build();
    }
}
