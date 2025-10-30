package com.example.nationalfitnessapp.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenAIRequestDto {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    public OpenAIRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = List.of(new Message("user", prompt));
        this.temperature = 0.7;
        this.max_tokens = 100;
    }

    @Getter
    private static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
