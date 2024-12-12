package com.fifteen.eureka.message.infrastructure;

import com.fifteen.eureka.message.domain.entity.Message;
import com.fifteen.eureka.message.domain.repository.MessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepositoryImpl extends JpaRepository<Message,Long>, MessageRepository {
}
