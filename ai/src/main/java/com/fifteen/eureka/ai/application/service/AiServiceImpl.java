package com.fifteen.eureka.ai.application.service;

import com.fifteen.eureka.ai.application.dtos.AiCreateDeliveryEstimatedTimeRequestDto;
import com.fifteen.eureka.ai.infrastructure.util.AiChatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AiChatUtil aiChatUtil;

    @Override
    public String createDeliveryEstimatedTime(AiCreateDeliveryEstimatedTimeRequestDto aiCreateDeliveryEstimatedTimeRequestDto) {
        return aiChatUtil.getEstimatedTime(aiCreateDeliveryEstimatedTimeRequestDto);
    }
}
