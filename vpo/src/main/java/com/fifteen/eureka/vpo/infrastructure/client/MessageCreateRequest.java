package com.fifteen.eureka.vpo.infrastructure.client;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class MessageCreateRequest {

    private Long receiverId; // userId ?  슬랙id로 하면 안되나 !?
    private String message;
    private LocalDateTime sendTime;

}
