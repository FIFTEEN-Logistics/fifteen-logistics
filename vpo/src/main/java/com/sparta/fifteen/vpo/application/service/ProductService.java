package com.sparta.fifteen.vpo.application.service;

import com.sparta.fifteen.vpo.application.dto.product.CreateProductDto;
import com.sparta.fifteen.vpo.application.dto.product.ProductResponse;
import com.sparta.fifteen.vpo.application.dto.product.UpdateProductDto;
import com.sparta.fifteen.vpo.domain.model.Product;
import com.sparta.fifteen.vpo.domain.model.Vendor;
import com.sparta.fifteen.vpo.domain.repository.ProductRepository;
import com.sparta.fifteen.vpo.domain.repository.VendorRepository;
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

    public ProductResponse createProduct(CreateProductDto request) {

        Vendor vendor = vendorRepository.findById(request.getVendorId()).orElseThrow();

        Product product = Product.create(
                request.getHubId(),
                request.getProductName(),
                request.getProductPrice(),
                request.getQuantity(),
                vendor
        );

        productRepository.save(product);

        return ProductResponse.of(product);
    }

    public Page<ProductResponse> getProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);

        List<ProductResponse> contents = products.getContent().stream().map(ProductResponse::of).toList();

        return new PageImpl<>(contents, pageable, products.getSize());
    }

    public ProductResponse getProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto request) {

        Product product = productRepository.findById(productId).orElseThrow();

        product.update(
          request.getHubId(),
          request.getProductName(),
          request.getProductPrice(),
          request.getQuantity()
        );

        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow();

        product.delete();

        return ProductResponse.of(product);


    }
}
