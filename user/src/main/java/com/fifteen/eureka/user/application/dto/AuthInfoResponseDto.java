package com.fifteen.eureka.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthInfoResponseDto {
    private Long userId;
    private String username;
    private String role;
}
