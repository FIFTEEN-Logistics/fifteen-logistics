package com.fifteen.eureka.user.application.dto.deliveryManager;

import com.fifteen.eureka.user.application.dto.deliveryManager.DeliveryManagerCreateRequest.DeliveryManagerType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerUpdateRequest {
	private UUID hubId;
	private DeliveryManagerType deliveryManagerType;
}
