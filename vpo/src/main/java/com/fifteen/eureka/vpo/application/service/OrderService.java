package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.order.*;
import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.OrderDetail;
import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.repository.OrderRepository;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.CreateDeliveryDto;
import com.fifteen.eureka.vpo.infrastructure.client.DeliveryClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final DeliveryClient deliveryClient;

    @Transactional
    public OrderResponse createOrder(CreateOrderDto orderRequest, List<CreateOrderDetailDto> orderDetailsRequest, CreateDeliveryInfoDto deliveryRequest) {

        Vendor receiver = vendorRepository.findById(orderRequest.getReceiverId())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));


        Vendor supplier = vendorRepository.findById(orderRequest.getSupplierId())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));


        Order order = Order.create(
                orderRequest.getUserId(),
                orderRequest.getOrderRequest(),
                receiver,
                supplier
        );

//        // 배달 전달
//        CreateDeliveryDto createDeliveryDto = CreateDeliveryDto.builder()
//                .orderId(order.getOrderId())
//                .deliveryAddress(deliveryRequest.getDeliveryAddress())
//                .recipient(deliveryRequest.getRecipient())
//                .recipientSlackId(deliveryRequest.getRecipientSlackId())
//                .build();
//
//        //response 객체던 id만 받던
//        UUID deliveryId = Optional.ofNullable(deliveryClient.createDelivery(createDeliveryDto));

        order.addDelivery(UUID.fromString("a3765f91-67d8-42e8-97dc-8e851c29e049"));

        // orderDetail 추가
        for (CreateOrderDetailDto OrderDetailDto : orderDetailsRequest) {

            Product product = productRepository.findById(OrderDetailDto.getProductId())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

            order.addOrderDetails(
                    OrderDetail.create(order, product, OrderDetailDto.getQuantity())
            );

            product.updateQuantity(OrderDetailDto.getQuantity());
        }

        order.calculateTotalPrice();


        //주문번호 로직 구현필요
        order.addOrderNumber("orderNumber");

        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    public Page<OrderResponse> getOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        List<OrderResponse> contents = orders.getContent().stream().map(OrderResponse::of).toList();

        return new PageImpl<>(contents, pageable, orders.getSize());
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderResponse.of(order);
    }

    //주문수정
    //- 주문 id, 배달 id를 유지하며 수정하려면
    //   ( 상품 준비중 일떄만 가능)
    //
    //    1. 상품관련, 요청사항, 수령업체 변경 시 order 에서만 처리
    //    2. 요청업체 변경 시 배달에 전송? 배달 경로바뀌니까
    //        - 요청업체의 물품 확인 로직
    // 일단 1만 구현해놓기! 인데 사실
    // 수령업체가 바뀌는거랑 배송지는 큰 상관없을거같고.. 본사분사 무 ㅓ많으니..?

    @Transactional
    public OrderResponse updateOrder(UUID orderId, UpdateOrderDto orderRequest, List<UpdateOrderDetailDto> orderDetailsRequest) {

        //order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));


        // 공급업체 변경 처리
//        if (!order.getSupplier().getVendorId().equals(orderRequest.getSupplierId())) {
//            Vendor newSupplier = vendorRepository.findById(orderRequest.getSupplierId())
//                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
//            order.updateSupplier(newSupplier);
//        }

        // 수령업체 변경 처리
        if (!order.getReceiver().getVendorId().equals(orderRequest.getReceiverId())) {
            Vendor newReceiver = vendorRepository.findById(orderRequest.getReceiverId())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
            order.updateReceiver(newReceiver);
        }

        // OrderDetail 변경 처리
//        if (isOrderDetailsChanged(order, orderDetailsRequest)) {
//
//        }

        // 요청사항 변경 처리
        if (!order.getOrderRequest().equals(orderRequest.getOrderRequest())) {
            order.updateOrderRequest(orderRequest.getOrderRequest());
        }

        order.calculateTotalPrice();

        return OrderResponse.of(order);

    }
    @Transactional
    public OrderResponse cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
        order.cancel();
        return OrderResponse.of(order);
    }


    public OrderResponse deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
        orderRepository.delete(order);
        return OrderResponse.of(order);
    }

}
