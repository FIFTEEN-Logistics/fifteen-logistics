package com.fifteen.eureka.message.application.service;

import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.application.dtos.MessageUpdateRequestDto;

import java.util.UUID;

public interface MessageService {

    void createMessage(MessageCreateRequestDto messageCreateRequestDto);

    void updateMessage(MessageUpdateRequestDto messageUpdateRequestDto, UUID messageId);

    void deleteMessage(UUID messageId);
}
