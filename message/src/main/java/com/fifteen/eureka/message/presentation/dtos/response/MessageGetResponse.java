package com.fifteen.eureka.message.presentation.dtos.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MessageGetResponse {

    private UUID messageId;
    private Long receiverId;
    private String message;
    private LocalDateTime sendTime;

    @QueryProjection
    public MessageGetResponse(UUID messageId, Long receiverId, String message, LocalDateTime sendTime) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.message = message;
        this.sendTime = sendTime;
    }
}
