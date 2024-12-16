package com.fifteen.eureka.user.application.dto.deliveryManager;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class DeliveryManagerCreateRequest {
  @NonNull
  private Long userId;
  private UUID hubId; // ROLE_DELIVERY_HUB일 경우 허브 ID
  @NonNull
  private DeliveryManagerType deliveryManagerType;

  public enum DeliveryManagerType {
    HUB, // ROLE_DELIVERY_HUB
    VENDOR // ROLE_DELIVERY_VENDOR
  }
}
