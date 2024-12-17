package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<Order, UUID>, OrderRepository {
}
