package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Order> findByKeyword(String keyword, Pageable pageable, Long currentUserId, boolean isHubManager) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();

        if (isHubManager) {
            builder.and(order.supplier.hubManagerId.eq(currentUserId)
                    .or(order.receiver.hubManagerId.eq(currentUserId)));
        } else {
            builder.and(order.userId.eq(currentUserId));
        }

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(order.orderRequest.containsIgnoreCase(keyword)
                    .or(order.orderNumber.containsIgnoreCase(keyword)));
        }

        List<Order> orders = jpaQueryFactory
                .selectFrom(order)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(order.count())
                        .from(order)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(orders, pageable, total);
    }
}
