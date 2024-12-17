package com.fifteen.eureka.vpo.presentation.request.product;

import com.fifteen.eureka.vpo.application.dto.product.CreateProductDto;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateProductRequest {

    @NotNull(message = "업체 ID는 필수입니다.")
    private UUID vendorId;

    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(max = 100, message = "상품 이름은 최대 100자까지 입력할 수 있습니다.")
    private String productName;

    @NotNull(message = "상품 가격은 필수입니다.")
    @Positive(message = "상품 가격은 0보다 커야 합니다.")
    private int productPrice;

    @NotNull(message = "상품 수량은 필수입니다.")
    @PositiveOrZero(message = "상품 수량은 0 이상이어야 합니다.")
    private int quantity;

    public CreateProductDto toDto() {
        return CreateProductDto.create(this.vendorId, this.productName, this.productPrice, this.quantity);
    }


}
