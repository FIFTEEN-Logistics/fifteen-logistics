package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.vpo.application.dto.order.CreateDeliveryInfoDto;
import com.fifteen.eureka.vpo.application.dto.order.CreateOrderDetailDto;
import com.fifteen.eureka.vpo.application.dto.order.CreateOrderDto;
import com.fifteen.eureka.vpo.application.dto.order.OrderResponse;
import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.OrderDetail;
import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.repository.OrderRepository;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
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
    private final OrderedFormContentFilter formContentFilter;
    private final ProductService productService;

    @Transactional
    public OrderResponse createOrder(CreateOrderDto orderRequest, List<CreateOrderDetailDto> orderDetailsRequest, CreateDeliveryInfoDto deliveryRequest) {

        Vendor receiver = vendorRepository.findById(orderRequest.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));


        Vendor supplier = vendorRepository.findById(orderRequest.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));


        Order order = Order.create(
                orderRequest.getUserId(),
                orderRequest.getOrderRequest(),
                receiver,
                supplier
        );

        for (CreateOrderDetailDto OrderDetailDto : orderDetailsRequest) {

            Product product = productRepository.findById(OrderDetailDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            order.addOrderDetails(
                    OrderDetail.create(order, product, OrderDetailDto.getQuantity())
            );

            product.updateQuantity(product.getQuantity() - OrderDetailDto.getQuantity());
        }

        order.calculateTotalPrice();

        order.addDelivery(UUID.fromString("a3765f91-67d8-42e8-97dc-8e851c29e049"));

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

    @Transactional
    public OrderResponse cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.cancel();
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

//    @Transactional
//    public OrderResponse updateOrder(UUID orderId, UpdateOrderDto orderRequest, List<UpdateOrderDetailDto> orderDetailsRequest) {
//
//
//    }


}
