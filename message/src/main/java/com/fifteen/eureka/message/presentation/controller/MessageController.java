package com.fifteen.eureka.message.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.application.service.MessageService;
import com.fifteen.eureka.message.presentation.dtos.request.MessageCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping()
    public ApiResponse<?> createMessage(
            @Valid @RequestBody MessageCreateRequest messageCreateRequest) {

        log.info("메시지 생성 URL 매핑 : OK");
        messageService.createMessage(MessageCreateRequestDto.from(messageCreateRequest));
        return ApiResponse.OK(ResSuccessCode.CREATED);
    }
}
