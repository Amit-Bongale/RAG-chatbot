package com.example.chatbot.DTO;

public record ChatRequest(
        String sessionId,
        String message
) {
}
