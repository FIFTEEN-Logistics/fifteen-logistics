package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.vpo.application.dto.order.*;
import com.fifteen.eureka.vpo.domain.model.*;
import com.fifteen.eureka.vpo.domain.repository.OrderRepository;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.domain.service.OrderProductService;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryCreateRequest;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryClient;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryCreateResponse;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.message.MessageClient;
import com.fifteen.eureka.vpo.infrastructure.client.message.MessageCreateRequest;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserClient;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserGetResponseDto;
import com.fifteen.eureka.vpo.infrastructure.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final DeliveryClient deliveryClient;
    private final MessageClient messageClient;
    private final OrderProductService orderProductService;
    private final UserClient userClient;
    @Value("${UserCheck.key}")
    private String userCheckKey;

    @Transactional
    public OrderResponse createOrder(CreateOrderDto orderRequest, List<CreateOrderDetailDto> orderDetailsRequest, CreateDeliveryInfoDto deliveryRequest, Long currentUserId) {

        // 업체 타입 확인
        Vendor receiver = checkVendorType(orderRequest.getReceiverId(), VendorType.RECEIVER);
        Vendor supplier = checkVendorType(orderRequest.getSupplierId(), VendorType.SUPPLIER);

        Order order = Order.create(
                orderRequest.getUserId(),
                orderRequest.getOrderRequest(),
                receiver,
                supplier
        );


        // 배달 전달
        DeliveryCreateRequest deliveryCreateRequest = DeliveryCreateRequest.builder()
                .orderId(order.getOrderId())
                .startHubId(order.getSupplier().getHubId())
                .endHubId(order.getReceiver().getHubId())
                .deliveryAddress(deliveryRequest.getDeliveryAddress())
                .recipient(deliveryRequest.getRecipient())
                .recipientSlackId(deliveryRequest.getRecipientSlackId())
                .build();


        DeliveryCreateResponse deliveryCreateResponse = Optional.ofNullable(
                (DeliveryCreateResponse) deliveryClient.createDelivery(deliveryCreateRequest).getBody())
                .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

//        DeliveryCreateResponse deliveryCreateResponse = Optional.ofNullable(
//                (DeliveryCreateResponse) deliveryClient.createDelivery(deliveryCreateRequest).getData()).orElseThrow(() -> {
//                    String message = deliveryClient.createDelivery(deliveryCreateRequest).getMessage();
//
//                    if ("해당 시퀀스의 허브 배송 담당자가 없습니다.".equals(message)) {
//                        cancelOrder(order.getOrderId(), currentUserId);
//                    } else if ("해당 시퀀스의 업체 배송 담당자가 없습니다.".equals(message)) {
//                        cancelOrder(order.getOrderId(), currentUserId);
//                    }
//                    return new CustomApiException(ResErrorCode.BAD_REQUEST);
//                }
//        );
//

        // orderDetail 추가
        for (CreateOrderDetailDto OrderDetailDto : orderDetailsRequest) {

            Product product = productRepository.findByProductIdAndVendor_VendorId(OrderDetailDto.getProductId(), supplier.getVendorId())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체에 해당 상품이 존재하지 않습니다."));

            if(product.getQuantity() - OrderDetailDto.getQuantity() < 0 ) {
                throw new CustomApiException(ResErrorCode.BAD_REQUEST, "주문 상품의 수는 상품의 재고를 넘을 수 없습니다.");
            }

            order.addOrderDetails(
                    OrderDetail.create(order, product, OrderDetailDto.getQuantity())
            );

            orderProductService.updateProduct(product,OrderDetailDto.getQuantity(), order.isCanceled());

        }

        order.calculateTotalPrice();

        orderRepository.save(order);

        UserGetResponseDto userGetResponseDto = Objects.requireNonNull(
                userClient.findUserById(currentUserId, "username", userCheckKey).getBody()).getData();

        // 메시지 전송
        MessageCreateRequest request = MessageCreateRequest.builder()
                // 주문 정보
                .receiverId(order.getSupplier().getHubManagerId())
                .OrderNumber(order.getOrderNumber())
                .productDetails(order.getOrderDetails().stream().map(MessageCreateRequest.ProductDetail::of).toList())
                .orderRequest(order.getOrderRequest())
                // 주문 생성 시 배달 요청 정보
                .recipientSlackId(deliveryRequest.getRecipientSlackId())
                // 주문자 정보
                .userName(userGetResponseDto.getUsername())
                .userEmail(userGetResponseDto.getEmail())
                // 배달 정보
                .departureHubName(deliveryCreateResponse.getDepartureHubName())
                .routingHubNames(deliveryCreateResponse.getRoutingHubNames())
                .deliveryAddress(deliveryCreateResponse.getDeliveryAddress())
                .deliveryUserName(deliveryCreateResponse.getDeliveryUserName())
                .deliveryUserEmail(deliveryCreateResponse.getDeliveryUserEmail())
                .build();

        messageClient.createMessage(request);


        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Pageable pageable, String keyword, Long currentUserId, String currentRole) {

        boolean isHubManager = currentRole.equals("ROLE_ADMIN_HUB");

        Page<Order> orders = orderQueryRepository.findByKeyword(keyword, pageable, currentUserId, isHubManager);

        List<OrderResponse> contents = orders.getContent().stream().map(OrderResponse::of).toList();

        return new PageImpl<>(contents, pageable, orders.getSize());
    }

    public OrderResponse getOrder(UUID orderId, Long currentUserId, String currentRole) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!order.getSupplier().getHubManagerId().equals(currentUserId) && !order.getReceiver().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        } else {
            if (!order.getUserId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        return OrderResponse.of(order);
    }

    @Transactional
    public OrderResponse updateOrder(UUID orderId, UpdateOrderDto orderRequest, Long currentUserId, String currentRole) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!order.getSupplier().getHubManagerId().equals(currentUserId) && !order.getReceiver().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        //배송 상태 확인
        DeliveryDetailsResponse deliveryDetailsResponse = (DeliveryDetailsResponse) Optional.ofNullable(deliveryClient.getDelivery(order.getDeliveryId()).getBody())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if(!deliveryDetailsResponse.getDeliveryStatus().equals(DeliveryDetailsResponse.DeliveryStatus.HUB_WAITING)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 주문은 배송 시작상태로 수정이 불가합니다.");
        }

        // 수령업체 변경 처리 -> 배달, 슬랙메시지 전송
        if (!order.getReceiver().getVendorId().equals(orderRequest.getReceiverId())) {

            Vendor receiver = checkVendorType(orderRequest.getReceiverId(), VendorType.RECEIVER);

            order.updateReceiver(receiver);
        }

        // 요청사항 변경 처리 -> 배달, 슬랙메시지 전송
        if (!order.getOrderRequest().equals(orderRequest.getOrderRequest())) {
            order.updateOrderRequest(orderRequest.getOrderRequest());
        }

        order.calculateTotalPrice();

        return OrderResponse.of(order);

    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId, Long currentUserId, String currentRole) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!order.getSupplier().getHubManagerId().equals(currentUserId) && !order.getReceiver().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        //배송 상태 확인
        DeliveryDetailsResponse deliveryDetailsResponse = (DeliveryDetailsResponse) Optional.ofNullable(deliveryClient.getDelivery(order.getDeliveryId()).getBody())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if(!deliveryDetailsResponse.getDeliveryStatus().equals(DeliveryDetailsResponse.DeliveryStatus.HUB_WAITING)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 주문은 배송 시작상태로 취소가 불가합니다.");
        }


        if(order.isCanceled()) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 주문은 이미 취소된 주문입니다.");
        }

        order.cancel();

        for (OrderDetail orderDetail : order.getOrderDetails()) {

            Product product = productRepository.findById(orderDetail.getProduct().getProductId())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

            orderProductService.updateProduct(product,orderDetail.getQuantity(), order.isCanceled());
        }


        return OrderResponse.of(order);

    }


    public OrderResponse deleteOrder(UUID orderId, Long currentUserId, String currentRole) {
        //배송 상태 확인
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!order.getSupplier().getHubManagerId().equals(currentUserId) && !order.getReceiver().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        DeliveryDetailsResponse deliveryDetailsResponse = (DeliveryDetailsResponse) Optional.ofNullable(deliveryClient.getDelivery(order.getDeliveryId()).getBody())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if(!deliveryDetailsResponse.getDeliveryStatus().equals(DeliveryDetailsResponse.DeliveryStatus.HUB_WAITING)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 주문은 배송 시작상태로 삭제가 불가합니다.");
        }

        orderRepository.delete(order);

        return OrderResponse.of(order);

    }

    private Vendor checkVendorType(UUID vendorId, VendorType vendorType) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (!vendor.getVendorType().equals(vendorType)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 업체는 " + vendorType.getLabel() + "가 아닙니다.");
        }

        return vendor;
    }

}
