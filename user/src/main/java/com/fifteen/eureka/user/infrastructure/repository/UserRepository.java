package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.domain.model.QUser;
import com.fifteen.eureka.user.domain.model.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom,
    QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  @Override
  default void customize(QuerydslBindings bindings, QUser root) {
    bindings.bind(String.class).all((path, values) -> {
      BooleanBuilder builder = new BooleanBuilder();
      if (path instanceof StringPath stringPath) {
        values.forEach(value -> builder.or(stringPath.containsIgnoreCase(value)));
      }
      return Optional.of(builder);
    });
  }
}