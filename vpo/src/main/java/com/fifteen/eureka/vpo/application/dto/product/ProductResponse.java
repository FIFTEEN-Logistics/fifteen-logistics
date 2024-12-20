package com.fifteen.eureka.vpo.application.dto.product;

import com.fifteen.eureka.vpo.domain.model.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductResponse {
    private UUID productId;
    private UUID vendorId;
    private UUID hubId;
    private String productName;
    private int productPrice;
    private int quantity;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .vendorId(product.getVendor().getVendorId())
                .hubId(product.getHubId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
