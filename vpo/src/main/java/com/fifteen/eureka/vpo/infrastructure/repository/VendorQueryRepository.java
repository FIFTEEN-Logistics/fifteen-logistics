package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.QVendor;
import com.fifteen.eureka.vpo.domain.model.Vendor;
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
public class VendorQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;


    public Page<Vendor> findByKeyword(String keyword, Pageable pageable) {

        QVendor vendor = QVendor.vendor;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(vendor.vendorName.containsIgnoreCase(keyword)
                    .or(vendor.vendorAddress.containsIgnoreCase(keyword)));
        }

        List<Vendor> vendors = jpaQueryFactory
                .selectFrom(vendor)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(vendor.count())
                        .from(vendor)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(vendors, pageable, total);

    }
}
