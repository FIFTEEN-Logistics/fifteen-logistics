package com.fifteen.eureka.delivery.infrastructure.repository;

import static com.fifteen.eureka.delivery.domain.model.QDelivery.*;
import static com.fifteen.eureka.delivery.domain.model.QDeliveryManager.*;
import static com.fifteen.eureka.delivery.domain.model.QDeliveryRoute.*;
import static com.fifteen.eureka.delivery.domain.model.QHub.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fifteen.eureka.delivery.application.dto.delivery.DeliverySimpleResponse;
import com.fifteen.eureka.delivery.common.role.Role;
import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.domain.model.QDeliveryManager;
import com.fifteen.eureka.delivery.domain.model.QDeliveryRoute;
import com.fifteen.eureka.delivery.domain.model.QHub;
import com.fifteen.eureka.delivery.infrastructure.util.QuerydslUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<DeliverySimpleResponse> findAllByRole(Pageable pageable, Role role, Long userId) {

		queryFactory
			.selectFrom(delivery)
			.join(delivery.deliveryRoutes, deliveryRoute)
			.join(deliveryRoute.deliveryManager, deliveryManager)
			.join(delivery.startHub, hub)
			.join(delivery.endHub, hub)
			.where(
				hubManagerIdEq(role, userId),

			)
			.orderBy(QuerydslUtils.getSort(pageable, delivery))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	BooleanExpression hubManagerIdEq(Role role, Long userId) {
		return role == Role.ROLE_ADMIN_HUB ?
			deliveryRoute.departureHub.hubManagerId.eq(userId).or(deliveryRoute.arrivalHub.hubManagerId.eq(userId)) :
			null;
	}

	BooleanExpression selfisei(Role role, Long userId) {
		return role == Role.ROLE_DELIVERY_HUB ?
			deliveryRoute.deliveryManager.id
	}


}
