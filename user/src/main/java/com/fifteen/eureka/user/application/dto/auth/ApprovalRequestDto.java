package com.fifteen.eureka.user.application.dto.auth;

import com.fifteen.eureka.user.domain.model.ApprovalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequestDto {

  @NotNull(message = "Approval status is required.")
  private ApprovalStatus approvalStatus;
}
