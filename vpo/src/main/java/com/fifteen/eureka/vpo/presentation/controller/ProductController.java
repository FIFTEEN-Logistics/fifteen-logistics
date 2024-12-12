package com.fifteen.eureka.vpo.presentation.controller;


import com.fifteen.eureka.vpo.application.dto.product.ProductResponse;
import com.fifteen.eureka.vpo.application.service.ProductService;
import com.fifteen.eureka.vpo.presentation.request.product.UpdateProductRequest;
import com.fifteen.eureka.vpo.presentation.request.product.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request.toDto()));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(@PageableDefault(sort = "productPrice") Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID productId,
            @RequestBody UpdateProductRequest request) {

        return ResponseEntity.ok(productService.updateProduct(productId, request.toDto()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
