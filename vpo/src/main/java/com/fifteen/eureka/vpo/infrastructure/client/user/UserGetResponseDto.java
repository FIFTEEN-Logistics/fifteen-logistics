package com.fifteen.eureka.vpo.infrastructure.client.user;

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

    public enum Role {
        ROLE_ADMIN_MASTER,
        ROLE_ADMIN_VENDOR,
        ROLE_ADMIN_HUB,
        ROLE_DELIVERY_HUB,
        ROLE_DELIVERY_VENDOR;
    }
}

