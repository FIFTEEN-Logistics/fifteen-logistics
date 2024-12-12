package com.fifteen.eureka.message.domain.repository;

import com.fifteen.eureka.message.domain.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(UUID messageId);

    void deleteById(UUID messageId);
}
