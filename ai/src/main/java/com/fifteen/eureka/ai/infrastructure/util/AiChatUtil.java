package com.fifteen.eureka.ai.infrastructure.util;

import com.fifteen.eureka.ai.application.dtos.AiCreateDeliveryEstimatedTimeRequestDto;

public interface AiChatUtil {

    String getEstimatedTime(AiCreateDeliveryEstimatedTimeRequestDto aiCreateDeliveryEstimatedTimeRequestDto);
}
