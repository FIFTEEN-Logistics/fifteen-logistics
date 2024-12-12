package com.sparta.fifteen.vpo.application.service;

import com.sparta.fifteen.vpo.application.dto.order.CreateDeliveryInfoDto;
import com.sparta.fifteen.vpo.application.dto.order.CreateOrderDetailDto;
import com.sparta.fifteen.vpo.application.dto.order.CreateOrderDto;
import com.sparta.fifteen.vpo.application.dto.order.OrderResponse;
import com.sparta.fifteen.vpo.application.dto.product.ProductResponse;
import com.sparta.fifteen.vpo.domain.model.Order;
import com.sparta.fifteen.vpo.domain.model.OrderDetail;
import com.sparta.fifteen.vpo.domain.model.Product;
import com.sparta.fifteen.vpo.domain.model.Vendor;
import com.sparta.fifteen.vpo.domain.repository.OrderRepository;
import com.sparta.fifteen.vpo.domain.repository.ProductRepository;
import com.sparta.fifteen.vpo.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final OrderedFormContentFilter formContentFilter;
    private final ProductService productService;

    @Transactional
    public OrderResponse createOrder(CreateOrderDto orderRequest, List<CreateOrderDetailDto> orderDetailsRequest, CreateDeliveryInfoDto deliveryRequest) {
        // 주문 생성
        //Vendor supplier = order.getOrderDetails().get(0).getProduct().getVendor();
        Vendor receiver = vendorRepository.findById(orderRequest.getSupplierId()).orElseThrow();
        Vendor supplier = vendorRepository.findById(orderRequest.getReceiverId()).orElseThrow();

        Order order = Order.create(
                orderRequest.getUserId(),
                orderRequest.getOrderRequest(),
                receiver,
                supplier
        );

        long totalPrice = 0;

        // for 문으로 한 리스트씩 돌면서 product 검사 & 같은 supplierId 인지 체크

        for (CreateOrderDetailDto OrderDetailDto : orderDetailsRequest) {
            Product product = productRepository.findById(OrderDetailDto.getProductId()).orElseThrow();
            long productsPrice = (long) product.getProductPrice() * OrderDetailDto.getQuantity();
            totalPrice += productsPrice;
            order.addOrderDetails(
                    OrderDetail.create(order, product, OrderDetailDto.getQuantity(), productsPrice)
            );
            // product 재고에서 orderDetails quantity 총 합 빼기
            product.updateQuantity(product.getQuantity() - OrderDetailDto.getQuantity());
        }
        // delivery 생성
        order.addDelivery(UUID.fromString("a3765f91-67d8-42e8-97dc-8e851c29e049"));

        // 총가격 추가
        order.addTotalPrice( totalPrice);
        // 주문번호 추가
        order.addOrderNumber("orderNumber");
        // 주문 저장
        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    public Page<OrderResponse> getOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        List<OrderResponse> contents = orders.getContent().stream().map(OrderResponse::of).toList();

        return new PageImpl<>(contents, pageable, orders.getSize());
    }

    public Object getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderResponse.of(order);
    }

    // 주문 수정은 배달이 상품 준비중일때만 가능
    // 1. 요청사항
    // 2. delivery 정보를 수정하면 배달에도 다시 전달을 해야함
    // 3. 수량을 조정하면 orderDetail 수정
    // 주문취소 put
    // 주문을 .. .. 배송담당자
}
