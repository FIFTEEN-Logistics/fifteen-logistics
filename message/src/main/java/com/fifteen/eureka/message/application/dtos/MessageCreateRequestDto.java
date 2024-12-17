package com.fifteen.eureka.message.application.dtos;

import com.fifteen.eureka.message.presentation.dtos.request.MessageCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {

    private Long receiverId;
    private String messengerId;
    private String message;

    public static MessageCreateRequestDto from(MessageCreateRequest messageCreateRequest) {
        return MessageCreateRequestDto.builder()
                .receiverId(messageCreateRequest.getReceiverId())
                .messengerId(messageCreateRequest.getMessengerId())
                .message(messageCreateRequest.getMessage())
                .build();
    }
}
