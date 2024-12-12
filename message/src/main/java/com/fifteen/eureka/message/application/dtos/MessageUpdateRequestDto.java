package com.fifteen.eureka.message.application.dtos;

import com.fifteen.eureka.message.presentation.dtos.request.MessageUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MessageUpdateRequestDto {

    private Long receiverId;
    private String message;
    private LocalDateTime sendTime;

    public static MessageUpdateRequestDto from(MessageUpdateRequest messageUpdateRequest) {
        return MessageUpdateRequestDto.builder()
                .receiverId(messageUpdateRequest.getReceiverId())
                .message(messageUpdateRequest.getMessage())
                .sendTime(messageUpdateRequest.getSendTime())
                .build();
    }
}
