package com.fifteen.eureka.delivery.infrastructure.repository;

import static com.fifteen.eureka.delivery.domain.model.QHub.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fifteen.eureka.delivery.application.dto.hub.HubSimpleResponse;
import com.fifteen.eureka.delivery.application.dto.hub.QHubSimpleResponse;
import com.fifteen.eureka.delivery.infrastructure.util.QuerydslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRepositoryCustomImpl implements HubRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<HubSimpleResponse> findAllWithSearch(Pageable pageable, String search) {
		List<HubSimpleResponse> result = queryFactory
			.select(new QHubSimpleResponse(
				hub.id,
				hub.hubName,
				hub.hubName,
				hub.hubManagerId
			))
			.from(hub)
			.where(containsSearch(search))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(QuerydslUtils.getSort(pageable, hub))
			.fetch();

		Long count = queryFactory
			.select(hub.count())
			.from(hub)
			.where(containsSearch(search))
			.fetchOne();

		return new PageImpl<>(result, pageable, count);
	}

	private BooleanBuilder containsSearch(String search) {
		BooleanBuilder builder = new BooleanBuilder();
		if (search != null && !search.trim().isEmpty()) {
			builder.or(hub.hubName.containsIgnoreCase(search));
			builder.or(hub.hubAddress.containsIgnoreCase(search));
		}
		return builder;
	}
}
