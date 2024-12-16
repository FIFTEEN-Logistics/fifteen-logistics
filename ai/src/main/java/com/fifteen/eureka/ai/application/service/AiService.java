package com.fifteen.eureka.ai.application.service;

import com.fifteen.eureka.ai.application.dtos.AiCreateDeliveryEstimatedTimeRequestDto;

public interface AiService {

    String createDeliveryEstimatedTime(AiCreateDeliveryEstimatedTimeRequestDto requestDto);
}
