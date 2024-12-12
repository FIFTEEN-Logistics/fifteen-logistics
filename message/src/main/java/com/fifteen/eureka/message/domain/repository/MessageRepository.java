package com.fifteen.eureka.message.domain.repository;

import com.fifteen.eureka.message.domain.entity.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository {

    Message save(Message message);
}
