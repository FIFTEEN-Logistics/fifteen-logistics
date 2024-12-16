package com.fifteen.eureka.user.application.dto;

import com.fifteen.eureka.common.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

  @NotBlank(message = "Username is required.")
  @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters.")
  @Pattern(regexp = "^[a-z0-9]+$", message = "Username must contain only lowercase letters and numbers.")
  private String username;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
      message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
  )
  private String password;

  @NotBlank(message = "Email is required.")
  @Email(message = "Invalid email format.")
  private String email;

  @Size(max = 100, message = "Slack ID cannot exceed 100 characters.")
  private String slackId;

  @NotNull(message = "Role is required.")
  private Role role;

  private UUID hubId;
}
