package com.fifteen.eureka.ai.application.dtos;

import com.fifteen.eureka.ai.presentation.dtos.reqeust.AiCreateDeliveryEstimatedTimeRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AiCreateDeliveryEstimatedTimeRequestDto {

    private Long receiverId;
    private String messengerId;
    private String orderNumber;
    private String userName;
    private String userEmail;
    private List<AiCreateDeliveryEstimatedTimeRequest.ProductDetail> productDetails;
    private String orderRequest;
    private String departureHubName;
    private List<String> routingHubNames;
    private String deliveryAddress;
    private String deliveryUserName;
    private String deliveryUserEmail;

    public static AiCreateDeliveryEstimatedTimeRequestDto from(AiCreateDeliveryEstimatedTimeRequest aiCreateDeliveryEstimatedTimeRequest) {
        return AiCreateDeliveryEstimatedTimeRequestDto.builder()
                .receiverId(aiCreateDeliveryEstimatedTimeRequest.getReceiverId())
                .messengerId(aiCreateDeliveryEstimatedTimeRequest.getMessengerId())
                .orderNumber(aiCreateDeliveryEstimatedTimeRequest.getOrderNumber())
                .userName(aiCreateDeliveryEstimatedTimeRequest.getUserName())
                .userEmail(aiCreateDeliveryEstimatedTimeRequest.getUserEmail())
                .productDetails(aiCreateDeliveryEstimatedTimeRequest.getProductDetails())
                .orderRequest(aiCreateDeliveryEstimatedTimeRequest.getOrderRequest())
                .departureHubName(aiCreateDeliveryEstimatedTimeRequest.getDepartureHubName())
                .routingHubNames(aiCreateDeliveryEstimatedTimeRequest.getRoutingHubNames())
                .deliveryAddress(aiCreateDeliveryEstimatedTimeRequest.getDeliveryAddress())
                .deliveryUserName(aiCreateDeliveryEstimatedTimeRequest.getDeliveryUserName())
                .deliveryUserEmail(aiCreateDeliveryEstimatedTimeRequest.getDeliveryUserEmail())
                .build();
    }
    
}