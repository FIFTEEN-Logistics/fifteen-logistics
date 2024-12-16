package com.fifteen.eureka.ai.infrastructure.util.gemini;

import com.fifteen.eureka.ai.application.dtos.AiCreateDeliveryEstimatedTimeRequestDto;
import com.fifteen.eureka.ai.infrastructure.util.AiChatUtil;
import com.fifteen.eureka.ai.presentation.dtos.reqeust.AiCreateDeliveryEstimatedTimeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AiChatGemini implements AiChatUtil {

    private final RestTemplate restTemplate;

    @Value("${ai.chat.gemini.url}")
    private String geminiUrl;

    @Value("${ai.chat.gemini.key}")
    private String geminiKey;

    @Override
    public String getEstimatedTime(AiCreateDeliveryEstimatedTimeRequestDto aiCreateDeliveryEstimatedTimeRequestDto) {
        AiChatGeminiRequest aiChatGeminiRequest = new AiChatGeminiRequest(createQuestion(aiCreateDeliveryEstimatedTimeRequestDto));
        AiChatGeminiResponse aiChatGeminiResponse = restTemplate.postForObject(geminiUrl+geminiKey, aiChatGeminiRequest, AiChatGeminiResponse.class);
        String estimatedTime = aiChatGeminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
        return createAnswer(aiCreateDeliveryEstimatedTimeRequestDto,estimatedTime);
    }

    private String createQuestion(AiCreateDeliveryEstimatedTimeRequestDto aiCreateDeliveryEstimatedTimeRequestDto) {
        String question = "내가 자동차를 타고, 물건을 배송해야하는 상황인데 고객이 원하는 시간까지 배송을 완료해야해!!" + "\n" +
                "출발지에서 여러 경유지를 거쳐 도착지까지 가려고 해!" + "\n" +
                "출발지는 " + aiCreateDeliveryEstimatedTimeRequestDto.getDepartureHubName() + "\n" +
                "경유지는 차례로 " + aiCreateDeliveryEstimatedTimeRequestDto.getRoutingHubNames().toString() + "\n" +
                "목적지는 " + aiCreateDeliveryEstimatedTimeRequestDto.getDeliveryAddress() + "\n" +
                "고객이 원하는 시간은 " + aiCreateDeliveryEstimatedTimeRequestDto.getOrderRequest() + "\n" +
                "업무 시간을 9시~18시 고려해서 고객이 원하는 시간에 도착하기 위해서 출발지에서 몇 월, 몇 일, 몇 시에 출발해야 하는지 " + "\n" +
                "네이버 지도 또는 카카오 지도 또는 구글 지도에 검색을 통해 결과를 기반으로 \"월, 일, 시\" 로만 알려줘!";
        return question;
    }

    private String createAnswer(AiCreateDeliveryEstimatedTimeRequestDto aiCreateDeliveryEstimatedTimeRequestDto, String estimatedTime) {
        String answer = "주문 번호 : " + aiCreateDeliveryEstimatedTimeRequestDto.getOrderNumber() + "\n" +
        "주문자 정보 : " + aiCreateDeliveryEstimatedTimeRequestDto.getUserName() + " / " + aiCreateDeliveryEstimatedTimeRequestDto.getUserEmail() + "\n" +
        "상품 정보 : " + aiCreateDeliveryEstimatedTimeRequestDto.getProductDetails()
                .stream().map(AiCreateDeliveryEstimatedTimeRequest.ProductDetail::toString)
                .collect(Collectors.joining(" / "))+ "\n" +
        "요청 사항 : " + aiCreateDeliveryEstimatedTimeRequestDto.getOrderRequest() + "\n" +
        "발송지 : " + aiCreateDeliveryEstimatedTimeRequestDto.getDepartureHubName() + "\n" +
        "경유지 : " + aiCreateDeliveryEstimatedTimeRequestDto.getRoutingHubNames().toString() + "\n" +
        "도착지 : " + aiCreateDeliveryEstimatedTimeRequestDto.getDeliveryAddress() + "\n" +
        "배송담당자 : " + aiCreateDeliveryEstimatedTimeRequestDto.getDeliveryUserName() + "\n" +
                "------------------------------------------------------------" + "\n" +
                "최종 발송 시한 : " + estimatedTime;
        return answer;
    }
}
