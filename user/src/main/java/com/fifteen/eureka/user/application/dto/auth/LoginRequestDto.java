package com.fifteen.eureka.user.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
  private String username;
  private String password;
}
