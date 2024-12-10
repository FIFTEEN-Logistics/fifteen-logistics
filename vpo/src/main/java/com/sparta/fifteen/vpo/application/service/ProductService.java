package com.sparta.fifteen.vpo.application.service;

import com.sparta.fifteen.vpo.application.dto.product.CreateProductDto;
import com.sparta.fifteen.vpo.application.dto.product.ProductResponse;
import com.sparta.fifteen.vpo.domain.model.Product;
import com.sparta.fifteen.vpo.domain.model.Vendor;
import com.sparta.fifteen.vpo.domain.repository.ProductRepository;
import com.sparta.fifteen.vpo.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
