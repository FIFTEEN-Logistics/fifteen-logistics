package com.fifteen.eureka.message.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.application.dtos.MessageUpdateRequestDto;
import com.fifteen.eureka.message.domain.entity.Message;
import com.fifteen.eureka.message.domain.repository.MessageRepository;
import com.fifteen.eureka.message.infrastructure.util.MessageUtil;
import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Override
    @Transactional(readOnly = true)
    public Page<MessageGetResponse> getMessages(Pageable pageable, String search) {

        log.info("메시지 조회 서비스 : START, END");
        return messageRepository.findAllWithSearch(pageable, search);
    }

    @Override
    @Transactional
    public void updateMessage(MessageUpdateRequestDto messageUpdateRequestDto, UUID messageId) {

        log.info("메시지 수정 서비스 : START");

        Message message = messageRepository.findById(messageId)
                        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        message.update(messageUpdateRequestDto.getReceiverId(),
                messageUpdateRequestDto.getMessage(),
                messageUpdateRequestDto.getSendTime());

        log.info("메시지 수정 서비스 : END");
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {

        log.info("메시지 삭제 서비스 : START");
        messageRepository.deleteById(messageId);
        log.info("메시지 삭제 서비스 : END");
    }
}