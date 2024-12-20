package com.fifteen.eureka.vpo.infrastructure.client.ai;

import com.fifteen.eureka.vpo.domain.model.OrderDetail;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiCreateDeliveryEstimatedTimeRequest {

    private Long receiverId;

    private String messengerId;
    //=================ai 만들내용 ===================
    // 주문번호
    private String OrderNumber;
    // 주문자 정보
    private String userName;
    private String userEmail;
    // 상품 정보
    private List<ProductDetail> productDetails;
    // 요청 사항
    private String orderRequest;
    // 발송지
    private String departureHubName;
    // 경유지
    private List<String> routingHubNames;
    //도착지
    private String deliveryAddress;
    // 배송 담당자
    private String deliveryUserName;
    private String deliveryUserEmail;
    //=================ai 만들내용 ===================

    @Getter
    @Builder
    public static class ProductDetail {
        String productName;
        int quantity;

        public static ProductDetail of(OrderDetail orderDetail) {
            return ProductDetail.builder()
                    .productName(orderDetail.getProduct().getProductName())
                    .quantity(orderDetail.getQuantity())
                    .build();
        }
    }
}
