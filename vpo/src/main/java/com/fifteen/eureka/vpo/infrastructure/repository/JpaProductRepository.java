package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID>, ProductRepository {
}
