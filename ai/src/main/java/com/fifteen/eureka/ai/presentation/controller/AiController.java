package com.fifteen.eureka.ai.presentation.controller;

import com.fifteen.eureka.ai.application.dtos.AiCreateDeliveryEstimatedTimeRequestDto;
import com.fifteen.eureka.ai.application.service.AiService;
import com.fifteen.eureka.ai.presentation.dtos.reqeust.AiCreateDeliveryEstimatedTimeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping()
    public ResponseEntity<String> createDeliveryEstimatedTime(
            @Valid @RequestBody AiCreateDeliveryEstimatedTimeRequest aiCreateDeliveryEstimatedTimeRequest) {

        log.info("AI 예상 시간 확인 URL 매핑 : OK");
        String answer
                = aiService.createDeliveryEstimatedTime(AiCreateDeliveryEstimatedTimeRequestDto.from(aiCreateDeliveryEstimatedTimeRequest));
        return ResponseEntity.ok(answer);
    }
}
