package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.product.CreateProductDto;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.dto.product.UpdateProductDto;
import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductDto request) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 상품생성
        //vendor.getuserId != userid -> 자신의 업체만 상품 생성

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

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
    public Page<ProductResponse> getProducts(Pageable pageable, String keyword) {

        Page<Product> products = productRepository.findAll(pageable);

        List<ProductResponse> contents = products.getContent().stream().map(ProductResponse::of).toList();

        return new PageImpl<>(contents, pageable, products.getSize());
    }

    public ProductResponse getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto request) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 상품수정
        //vendor.getuserId != userid -> 자신의 업체만 상품 생성

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        product.update(
          request.getProductName(),
          request.getProductPrice(),
          request.getQuantity()
        );

        return ProductResponse.of(product);
    }

    public ProductResponse deleteProduct(UUID productId) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 상품삭제

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        productRepository.delete(product);

        return ProductResponse.of(product);

    }
}