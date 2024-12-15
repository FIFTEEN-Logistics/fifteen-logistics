package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository{
    Optional<Product> findByProductIdAndVendor_VendorId(UUID productId, UUID vendorId);

    Optional<Product> findById(UUID productId);

    void delete(Product product);

    void save(Product product);

    Page<Product> findAll(Pageable pageable);
}
