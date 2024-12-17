package com.fifteen.eureka.message.infrastructure;

import com.fifteen.eureka.message.infrastructure.util.QuerydslUtils;
import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import com.fifteen.eureka.message.presentation.dtos.response.QMessageGetResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fifteen.eureka.message.domain.entity.QMessage.message1;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MessageGetResponse> findAllWithSearch(Pageable pageable, String search) {
        List<MessageGetResponse> result = queryFactory
                .select(new QMessageGetResponse(
                    message1.messageId,
                    message1.receiverId,
                    message1.message,
                    message1.sendTime
                ))
                .from(message1)
                .where(containsSearch(search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QuerydslUtils.getSort(pageable,message1))
                .fetch();

        Long count = queryFactory
                .select(message1.count())
                .from(message1)
                .where(containsSearch(search))
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanBuilder containsSearch(String search) {
        BooleanBuilder builder = new BooleanBuilder();
        if (search != null && !search.trim().isEmpty()) {
            builder.or(message1.message.containsIgnoreCase(search));
        }
        return builder;
    }
}
