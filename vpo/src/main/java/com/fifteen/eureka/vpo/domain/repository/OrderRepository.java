package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository{
    Order save(Order order);

    Page<Order> findAll(Pageable pageable);

    Optional<Order> findById(UUID orderId);

    void delete(Order order);
}
