package com.fifteen.eureka.message.application.service;

import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.application.dtos.MessageUpdateRequestDto;
import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageService {

    void createMessage(MessageCreateRequestDto messageCreateRequestDto);

    Page<MessageGetResponse> getMessages(Pageable pageable, String search);

    void updateMessage(MessageUpdateRequestDto messageUpdateRequestDto, UUID messageId);

    void deleteMessage(UUID messageId);
}
