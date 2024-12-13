package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}