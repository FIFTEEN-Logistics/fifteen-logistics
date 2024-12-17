package com.fifteen.eureka.user.application.dto.user;

import com.fifteen.eureka.common.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {

  @Email(message = "Invalid email format.")
  private String email;

  @Size(max = 100, message = "Slack ID cannot exceed 100 characters.")
  private String slackId;

  private Role role;

  @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters.")
  @Pattern(
          regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
          message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
  )
  private String password;

  private UUID hubId;
}

