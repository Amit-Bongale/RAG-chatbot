package com.example.chatbot.DTO.chat;

public record ChatRequest(
        String sessionId,
        String message
) {
}
