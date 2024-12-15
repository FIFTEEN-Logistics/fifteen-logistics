package com.fifteen.eureka.vpo.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VendorQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
