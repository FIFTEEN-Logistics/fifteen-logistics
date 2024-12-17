package com.fifteen.eureka.message.infrastructure.util;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageUtil implements MessageUtil {

    private static final String MESSAGE_SENDER_NAME = "관리자";

    @Value("${slack.bot.token}")
    private String slackBotToken;

    private final MethodsClient methodsClient;

    @Override
    public void sendGeneralDirectMessage(String slackName, String message) {
        publishMessage(findBySlackName(slackName),message);
    }

    private String findBySlackName(String slackName) {

        try {
            return methodsClient
                    .usersList(request-> request.token(slackBotToken))
                    .getMembers().stream()
                    .filter(member -> member.getName().equals(slackName))
                    .map(User::getId).findFirst().orElseThrow(
                            () -> new CustomApiException(ResErrorCode.NOT_FOUND));
        } catch (IOException | SlackApiException e) {
            throw new CustomApiException(ResErrorCode.API_CALL_FAILED);
        }

    }

    private void publishMessage(String slackIdentityId, String message) {

        try {
            methodsClient.chatPostMessage(request -> request
                    .token(slackBotToken)
                    .channel(slackIdentityId)
                    .username(MESSAGE_SENDER_NAME)
                    .text(message));
        } catch (IOException | SlackApiException e) {
            throw new CustomApiException(ResErrorCode.API_CALL_FAILED);
        }
    }
}
