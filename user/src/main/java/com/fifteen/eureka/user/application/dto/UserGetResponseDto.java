package com.fifteen.eureka.user.application.dto;

import com.fifteen.eureka.common.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserGetResponseDto {
    private String username;
    private String email;
    private String slackId;
    private Role role;
}
