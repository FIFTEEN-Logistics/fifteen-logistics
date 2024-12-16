package com.fifteen.eureka.vpo.infrastructure.repository;

import com.fifteen.eureka.vpo.domain.model.QVendor;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class VendorQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;


    public Page<Vendor> findByKeyword(String keyword, Pageable pageable) {

        QVendor vendor = QVendor.vendor;

        List<Vendor> vendors = jpaQueryFactory
                .selectFrom(vendor).distinct()
                .where(
                        vendor.vendorName.containsIgnoreCase(keyword)
                                .or(vendor.vendorAddress.containsIgnoreCase(keyword))
                )
                .fetch();

        return new PageImpl<>(vendors, pageable, vendors.size());

    }
}
