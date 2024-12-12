package com.sparta.fifteen.vpo.domain.repository;

import com.sparta.fifteen.vpo.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
