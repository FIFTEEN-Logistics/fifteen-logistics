package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.application.dto.UserGetListResponseDto;
import com.fifteen.eureka.user.domain.model.QUser;
import com.fifteen.eureka.user.domain.model.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public UserGetListResponseDto findAllUsers(List<Long> idList, Predicate predicate,
      Pageable pageable) {
    QUser user = QUser.user;

    // 동적 조건 설정
    BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
    if (idList != null && !idList.isEmpty()) {
      booleanBuilder.and(user.id.in(idList));
    }

    // 메인 쿼리: 사용자 조회
    List<User> users = queryFactory
        .selectFrom(user)
        .where(booleanBuilder)
        .orderBy(user.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 총 개수 쿼리
    long totalCount = queryFactory
        .select(user.count())
        .from(user)
        .where(booleanBuilder)
        .fetchOne();

    // Page 객체 생성
    Page<User> userPage = PageableExecutionUtils.getPage(users, pageable, () -> totalCount);

    // DTO 변환
    return UserGetListResponseDto.of(userPage);
  }
}
