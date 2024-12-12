package com.sparta.fifteen.vpo.application.dto.product;

import com.sparta.fifteen.vpo.domain.model.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.type.descriptor.java.IntegerPrimitiveArrayJavaType;

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
    private boolean isDeleted;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .vendorId(product.getVendor().getVendorId())
                .hubId(product.getHubId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .quantity(product.getQuantity())
                .isDeleted(product.isDeleted())
                .build();
    }
}
