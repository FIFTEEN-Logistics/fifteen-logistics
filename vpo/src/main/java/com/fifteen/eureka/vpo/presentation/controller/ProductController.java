package com.fifteen.eureka.vpo.presentation.controller;


import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.service.ProductService;
import com.fifteen.eureka.vpo.infrastructure.util.PagingUtil;
import com.fifteen.eureka.vpo.presentation.request.product.CreateProductRequest;
import com.fifteen.eureka.vpo.presentation.request.product.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB", "ROLE_ADMIN_VENDOR"})
    @PostMapping
    public ApiResponse<?> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ApiResponse.OK(ResSuccessCode.CREATED, productService.createProduct(request.toDto()));
    }


    @GetMapping
    public ApiResponse<Page<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProducts(pageable, keyword));
    }

    @GetMapping("/{productId}")
    public ApiResponse<?> getProduct(@PathVariable UUID productId) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProduct(productId));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB", "ROLE_ADMIN_VENDOR"})
    @PutMapping("/{productId}")
    public ApiResponse<?> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, productService.updateProduct(productId, request.toDto()));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB", "ROLE_ADMIN_VENDOR"})
    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteProduct(@PathVariable UUID productId) {
        return ApiResponse.OK(ResSuccessCode.DELETED, productService.deleteProduct(productId));
    }
}
