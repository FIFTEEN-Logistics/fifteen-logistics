package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.QProduct;
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
public class ProductQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Product> findByKeyword(String keyword, Pageable pageable, Long currentUserId, boolean isHubManager) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.productName.containsIgnoreCase(keyword));
        }

        if (isHubManager) {
            builder.and(product.vendor.hubManagerId.eq(currentUserId));
        }

        List<Product> products = jpaQueryFactory
                .selectFrom(product)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(product.count())
                        .from(product)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(products, pageable, total);

    }
}
