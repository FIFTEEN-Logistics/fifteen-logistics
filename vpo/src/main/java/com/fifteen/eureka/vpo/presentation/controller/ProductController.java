package com.fifteen.eureka.vpo.presentation.controller;


import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.service.ProductService;
import com.fifteen.eureka.vpo.presentation.request.product.CreateProductRequest;
import com.fifteen.eureka.vpo.presentation.request.product.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<?> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ApiResponse.OK(ResSuccessCode.CREATED, productService.createProduct(request.toDto()));
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getProducts(@PageableDefault(sort = "productPrice") Pageable pageable) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProducts(pageable));
    }

    @GetMapping("/{productId}")
    public ApiResponse<?> getProduct(@PathVariable UUID productId) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProduct(productId));
    }

    @PutMapping("/{productId}")
    public ApiResponse<?> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, productService.updateProduct(productId, request.toDto()));
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteProduct(@PathVariable UUID productId) {
        return ApiResponse.OK(ResSuccessCode.DELETED, productService.deleteProduct(productId));
    }
}
