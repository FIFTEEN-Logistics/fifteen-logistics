package com.fifteen.eureka.message.application.dtos;

import com.fifteen.eureka.message.presentation.dtos.request.MessageCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {

    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime sendTime;

    public static MessageCreateRequestDto from(MessageCreateRequest messageCreateRequest) {
        return MessageCreateRequestDto.builder()
                .receiverId(messageCreateRequest.getReceiverId())
                .message(messageCreateRequest.getMessage())
                .sendTime(messageCreateRequest.getSendTime())
                .build();
    }
}
