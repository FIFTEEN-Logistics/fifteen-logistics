package com.fifteen.eureka.ai.presentation.dtos.reqeust;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AiCreateDeliveryEstimatedTimeRequest {

    @NotNull
    private Long receiverId;

    @NotBlank
    private String messengerId;

    @NotBlank
    private String orderNumber;

    @NotBlank
    private String userName;

    @NotBlank
    private String userEmail;

    @NotNull
    private List<ProductDetail> productDetails;

    @NotBlank
    private String orderRequest;

    @NotNull
    private String departureHubName;

    @NotNull
    private List<String> routingHubNames;

    @NotNull
    private String deliveryAddress;

    @NotBlank
    private String deliveryUserName;

    @Getter
    public static class ProductDetail {
        String productName;
        int quantity;

        @Override
        public String toString() {
            return productName + " - " + quantity;
        }
    }
}