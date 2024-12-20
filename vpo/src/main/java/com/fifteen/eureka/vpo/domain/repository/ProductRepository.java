package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository{

    Optional<Product> findByProductIdAndVendor_VendorId(UUID productId, UUID vendorId);

    Optional<Product> findById(UUID productId);

    void delete(Product product);

    Product save(Product product);

    Optional<Product> findByProductName(String productName);
}
