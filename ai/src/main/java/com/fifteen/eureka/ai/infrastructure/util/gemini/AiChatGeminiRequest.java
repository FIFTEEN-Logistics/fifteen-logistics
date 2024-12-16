package com.fifteen.eureka.ai.infrastructure.util.gemini;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AiChatGeminiRequest {

    private List<Content> contents;

    @Getter
    public static class Content {

        private List<Part> parts;

        public Content(String text) {
            parts = new ArrayList<>();
            Part part = new Part(text);
            parts.add(part);
        }
    }

    @Getter
    public static class Part {

        private String text;

        public Part(String text) {
            this.text = text;
        }
    }

    public AiChatGeminiRequest(String text) {
        this.contents = new ArrayList<>();
        Content content = new Content(text);
        contents.add(content);
    }
}
