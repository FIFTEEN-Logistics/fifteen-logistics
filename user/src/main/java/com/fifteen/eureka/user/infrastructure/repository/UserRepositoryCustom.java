package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.application.dto.UserGetListResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

  UserGetListResponseDto findAllUsers(List<Long> idList, Predicate predicate, Pageable pageable);
}
