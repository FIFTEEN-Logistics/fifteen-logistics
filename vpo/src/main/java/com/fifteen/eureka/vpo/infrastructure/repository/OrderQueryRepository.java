package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Order> findByKeyword(String keyword, Pageable pageable, Long currentUserId, boolean isHubManager) {

        QOrder order = QOrder.order;

        List<Order> orders = jpaQueryFactory
                .selectFrom(order).distinct()
                .where(
                        isHubManager ? order.supplier.hubManagerId.eq(currentUserId).or(order.receiver.hubManagerId.eq(currentUserId))
                                        .and(order.orderRequest.containsIgnoreCase(keyword)
                                        .or(order.orderNumber.containsIgnoreCase(keyword)))
                                    : order.userId.eq(currentUserId)
                                        .and(order.orderRequest.containsIgnoreCase(keyword)
                                        .or(order.orderNumber.containsIgnoreCase(keyword)))
                )
                .fetch();

        return new PageImpl<>(orders, pageable, orders.size());
    }
}
