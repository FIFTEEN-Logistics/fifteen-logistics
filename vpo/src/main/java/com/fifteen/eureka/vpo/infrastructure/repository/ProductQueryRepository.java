package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.Product;
import com.fifteen.eureka.vpo.domain.model.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Product> findByKeyword(String keyword, Pageable pageable, Long currentUserId, boolean isHubManager) {
        QProduct product = QProduct.product;

        List<Product> products = jpaQueryFactory
                .selectFrom(product).distinct()
                .where(
                        isHubManager ? product.vendor.hubManagerId.eq(currentUserId)
                                        .and(product.productName.containsIgnoreCase(keyword))
                                    : product.productName.containsIgnoreCase(keyword)
                )
                .fetch();

        return new PageImpl<>(products, pageable, products.size());
    }
}
