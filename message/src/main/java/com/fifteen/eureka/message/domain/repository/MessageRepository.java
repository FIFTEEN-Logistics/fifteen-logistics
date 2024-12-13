package com.fifteen.eureka.message.domain.repository;

import com.fifteen.eureka.message.domain.entity.Message;
import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository {

    Message save(Message message);

    Page<MessageGetResponse> findAllWithSearch(Pageable pageable, String search);

    Optional<Message> findById(UUID messageId);

    void deleteById(UUID messageId);
}
