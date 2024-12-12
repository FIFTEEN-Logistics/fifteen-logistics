package com.fifteen.eureka.message.application.service;

import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;

public interface MessageService {

    void createMessage(MessageCreateRequestDto messageCreateRequestDto);
}
