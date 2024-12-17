package com.fifteen.eureka.ai.infrastructure.util.gemini;

import lombok.Getter;

import java.util.List;

@Getter
public class AiChatGeminiResponse {

    private List<Candidate> candidates;

    @Getter
    public static class Candidate {
        private Content content;
        private String finishReason;
    }

    @Getter
    public static class Content {
        private List<Parts> parts;
        private String role;

    }

    @Getter
    public static class Parts {
        private String text;
    }
}
