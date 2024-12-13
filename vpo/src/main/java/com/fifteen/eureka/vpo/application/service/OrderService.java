package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.order.*;
import com.fifteen.eureka.vpo.domain.model.*;
import com.fifteen.eureka.vpo.domain.repository.OrderRepository;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.DeliveryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

        if(orderRequest.getReceiverId().equals(orderRequest.getSupplierId())) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "공급업체와 수령업체가 같습니다.");
        }

        Vendor receiver = checkVendorType(orderRequest.getReceiverId(), VendorType.RECEIVER);

        Vendor supplier = checkVendorType(orderRequest.getSupplierId(), VendorType.SUPPLIER);

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

            Product product = productRepository.findByProductIdAndVendor_VendorId(OrderDetailDto.getProductId(), supplier.getVendorId())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체에 해당 물건이 존재하지 않습니다."));

            if(product.getQuantity() - OrderDetailDto.getQuantity() < 0 ) {
                throw new CustomApiException(ResErrorCode.BAD_REQUEST, "주문 상품의 수는 상품의 재고를 넘을 수 없습니다.");
            }

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

            Vendor receiver = checkVendorType(orderRequest.getReceiverId(), VendorType.RECEIVER);

            order.updateReceiver(receiver);
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

        if(order.isCanceled()) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 주문은 이미 취소된 주문입니다.");
        }

        order.cancel();

        return OrderResponse.of(order);

    }


    public OrderResponse deleteOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

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
