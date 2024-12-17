package com.fifteen.eureka.vpo.infrastructure.client.user;

import com.fifteen.eureka.common.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGetResponseDto {
    private String username;
    private String email;
    private String slackId;
    private Role role;
}

