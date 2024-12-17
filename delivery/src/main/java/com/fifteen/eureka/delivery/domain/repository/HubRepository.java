package com.fifteen.eureka.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fifteen.eureka.delivery.domain.model.Hub;
import com.fifteen.eureka.delivery.infrastructure.repository.HubRepositoryCustom;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom{
}
