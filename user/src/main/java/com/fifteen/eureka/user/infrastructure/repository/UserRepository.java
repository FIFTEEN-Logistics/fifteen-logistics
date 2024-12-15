package com.fifteen.eureka.user.infrastructure.repository;

import com.fifteen.eureka.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
