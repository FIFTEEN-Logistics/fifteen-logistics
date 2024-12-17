package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.product.CreateProductDto;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.dto.product.UpdateProductDto;
import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryClient;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubClient;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.repository.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final ProductQueryRepository productQueryRepository;
    private final HubClient hubClient;
    private final DeliveryClient deliveryClient;

    @Transactional
    public ProductResponse createProduct(CreateProductDto request, Long currentUserId, String currentRole) {

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체를 찾을 수 없습니다."));

        productRepository.findByProductName(request.getProductName())
                .ifPresent(existingProduct -> {
                    if (Objects.equals(existingProduct.getVendor().getVendorId(), vendor.getVendorId())) {
                        throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 업체에 중복된 상품 이름이 존재합니다.");
                    }
                });


        if(!vendor.getVendorType().equals(VendorType.SUPPLIER)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 업체는 공급업체가 아닙니다.");
        }

        if (currentRole.equals("ROLE_ADMIN_HUB")) {

            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(hubClient.getHub(vendor.getHubId()).getData())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if(!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "담당 허브의 상품만 추가할 수 있습니다.");
            }

        }

        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if(!Objects.equals(vendor.getUserId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }


        Product product = Product.create(
                vendor.getHubId(),
                request.getProductName(),
                request.getProductPrice(),
                request.getQuantity(),
                vendor
        );

        productRepository.save(product);

        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public PagedModel<ProductResponse> getProducts(Pageable pageable, String keyword, Long currentUserId, String currentRole) {

        boolean isHubManager = currentRole.equals("ROLE_ADMIN_HUB");

        Page<Product> products = productQueryRepository.findByKeyword(keyword, pageable, currentUserId, isHubManager);

        List<ProductResponse> contents = products.getContent().stream().map(ProductResponse::of).toList();

        return new PagedModel<>(new PageImpl<>(contents, pageable, products.getTotalElements()));
    }

    public ProductResponse getProduct(UUID productId, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if(!Objects.equals(product.getVendor().getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "담당 허브의 상품만 조회할 수 있습니다.");
            }
        }

        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto request, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if(!Objects.equals(product.getVendor().getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "담당 허브의 상품만 수정할 수 있습니다.");
            }
        }

        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if(!Objects.equals(product.getVendor().getUserId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "담당 업체의 상품만 수정할 수 있습니다.");
            }
        }

        if (!product.getProductName().equals(request.getProductName())) {
            productRepository.findByProductName(request.getProductName())
                    .ifPresent(existingProduct -> {
                        throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 업체에 중복된 상품 이름이 존재합니다.");
                    });
        }

        product.update(
          request.getProductName(),
          request.getProductPrice(),
          request.getQuantity()
        );

        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse deleteProduct(UUID productId, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if(!Objects.equals(product.getVendor().getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "담당 허브의 상품만 삭제할 수 있습니다.");
            }
        }


        for (Order suppliedOrder : product.getVendor().getSuppliedOrders()) {
            if (!suppliedOrder.isCanceled()) {
                DeliveryDetailsResponse deliveryDetailsResponse = (DeliveryDetailsResponse) Optional.ofNullable(deliveryClient.getDelivery(suppliedOrder.getDeliveryId()).getData())
                        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 배송을 찾을 수 없습니다."));
                if (!deliveryDetailsResponse.getDeliveryStatus().equals(DeliveryDetailsResponse.DeliveryStatus.DST_ARRIVED)) {
                    throw new CustomApiException(ResErrorCode.BAD_REQUEST, "배달 진행중인 상품은 삭제할 수 없습니다.");
                }
            }
        }

        product.markAsDeleted();

        return ProductResponse.of(product);

    }
}
