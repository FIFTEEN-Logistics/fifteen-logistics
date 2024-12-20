package com.fifteen.eureka.message.presentation.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MessageCreateRequest {

    @NotNull
    private Long receiverId;

    @NotBlank
    private String messengerId;

    @NotBlank
    private String message;
}
