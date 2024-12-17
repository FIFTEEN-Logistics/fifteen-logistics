package com.fifteen.eureka.vpo.infrastructure.client.message;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {

    private Long receiverId;
    private String messengerId;
    private String message;

}
