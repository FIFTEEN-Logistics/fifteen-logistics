package com.fifteen.eureka.message.infrastructure;

import com.fifteen.eureka.message.domain.entity.Message;
import com.fifteen.eureka.message.domain.repository.MessageRepository;
import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepositoryImpl extends JpaRepository<Message, UUID>, MessageRepository, MessageRepositoryCustom {

    @Override
    Page<MessageGetResponse> findAllWithSearch(Pageable pageable, String search);
}
