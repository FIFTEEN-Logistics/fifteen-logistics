package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.domain.model.QUser;
import com.fifteen.eureka.user.domain.model.User;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.BooleanBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserJpaRepository extends JpaRepository<User, Long>,
    QuerydslPredicateExecutor<User>,
    QuerydslBinderCustomizer<QUser> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  @Override
  default void customize(QuerydslBindings querydslBindings, @NotNull QUser qUser) {
    querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
      List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
      if (valueList.isEmpty()) {
        return Optional.empty();
      }
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      for (String s : valueList) {
        booleanBuilder.or(path.containsIgnoreCase(s));
      }
      return Optional.of(booleanBuilder);
    });
  }
}
