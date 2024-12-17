package com.fifteen.eureka.user.domain.repository;

import com.fifteen.eureka.user.domain.model.User;
import com.querydsl.core.BooleanBuilder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long userId);

  User save(User user);

  void delete(User user);

  Page<User> findAll(BooleanBuilder booleanBuilder, Pageable pageable);
}
