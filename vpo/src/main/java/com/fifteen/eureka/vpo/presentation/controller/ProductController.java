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
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_ADMIN_HUB", "ROLE_ADMIN_VENDOR"})
    @PostMapping
    public ApiResponse<?> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.CREATED, productService.createProduct(request.toDto(), currentUserId, currentRole));
    }

    @GetMapping
    public ApiResponse<PagedModel<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProducts(pageable, keyword, currentUserId, currentRole));
    }

    @GetMapping("/{productId}")
    public ApiResponse<?> getProduct(
            @PathVariable UUID productId,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, productService.getProduct(productId, currentUserId, currentRole));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_ADMIN_HUB", "ROLE_ADMIN_VENDOR"})
    @PutMapping("/{productId}")
    public ApiResponse<?> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, productService.updateProduct(productId, request.toDto(), currentUserId, currentRole));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_ADMIN_HUB"})
    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteProduct(
            @PathVariable UUID productId,
            @RequestHeader("X-UserId") Long currentUserId,
            @RequestHeader("X-Role") String currentRole) {
        return ApiResponse.OK(ResSuccessCode.DELETED, productService.deleteProduct(productId, currentUserId, currentRole));
    }
}
