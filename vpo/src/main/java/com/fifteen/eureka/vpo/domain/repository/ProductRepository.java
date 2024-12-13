package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductIdAndVendor_VendorId(UUID productId, UUID vendorId);
}
