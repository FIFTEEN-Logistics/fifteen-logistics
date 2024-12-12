package com.sparta.fifteen.vpo.domain.repository;

import com.sparta.fifteen.vpo.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
