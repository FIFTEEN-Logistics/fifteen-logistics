package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.domain.model.User;
import com.fifteen.eureka.user.domain.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository jpaRepository;

  @Override
  public boolean existsByUsername(String username) {
    return jpaRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findById(Long userId) {
    return jpaRepository.findById(userId);
  }

  @Override
  public User save(User user) {
    return jpaRepository.save(user);
  }

  @Override
  public void delete(User user) {
    jpaRepository.delete(user);
  }

  @Override
  public Page<User> findAll(BooleanBuilder booleanBuilder, Pageable pageable) {
    return jpaRepository.findAll(booleanBuilder, pageable);
  }
}
