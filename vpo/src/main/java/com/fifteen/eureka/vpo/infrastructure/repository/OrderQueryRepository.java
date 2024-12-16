package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public PagedModel<Order> findByKeyword(String keyword, Pageable pageable) {

        QOrder order = QOrder.order;

        List<Order> orders = jpaQueryFactory
                .selectFrom(order).distinct()
                .where(
                        order.orderRequest.containsIgnoreCase(keyword)
                                .or(order.orderNumber.containsIgnoreCase(keyword))
                )
                .fetch();

        return new PagedModel<>(new PageImpl<>(orders, pageable, orders.size()));
    }
}
