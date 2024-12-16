package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.vpo.application.dto.product.CreateProductDto;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.dto.product.UpdateProductDto;
import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubClient;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserClient;
import com.fifteen.eureka.vpo.infrastructure.repository.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final ProductQueryRepository productQueryRepository;
    private final HubClient hubClient;
    private final UserClient userClient;


    @Transactional
    public ProductResponse createProduct(CreateProductDto request, Long currentUserId, String currentRole) {


        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {

            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                            hubClient.getHub(vendor.getHubId()).getBody())
                    .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if(!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }

        }

        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if(!vendor.getUserId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        if(!vendor.getVendorType().equals(VendorType.SUPPLIER)) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 업체는 공급업체가 아닙니다.");
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

        return new PagedModel<>(new PageImpl<>(contents, pageable, products.getSize()));
    }

    public ProductResponse getProduct(UUID productId, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if(!product.getVendor().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto request, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                            hubClient.getHub(product.getVendor().getHubId()).getBody())
                    .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if(!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }

        }

        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if(!product.getVendor().getUserId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        product.update(
          request.getProductName(),
          request.getProductPrice(),
          request.getQuantity()
        );

        return ProductResponse.of(product);
    }

    public ProductResponse deleteProduct(UUID productId, Long currentUserId, String currentRole) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if(product.getVendor().getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        productRepository.delete(product);

        return ProductResponse.of(product);

    }
}
