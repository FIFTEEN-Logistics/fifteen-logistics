package com.fifteen.eureka.message.application.service;

import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.domain.entity.Message;
import com.fifteen.eureka.message.domain.repository.MessageRepository;
import com.fifteen.eureka.message.infrastructure.util.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageUtil messageUtil;

    @Override
    @Transactional
    public void createMessage(MessageCreateRequestDto messageCreateRequestDto) {

        log.info("메시지 생성 서비스 : START");

        Message message = messageRepository.save(Message.create(
                messageCreateRequestDto.getReceiverId(),
                messageCreateRequestDto.getMessage(),
                messageCreateRequestDto.getSendTime()));

        message.sendGeneralDirectMessage(messageUtil);

        log.info("메시지 생성 서비스 : END");
    }
}