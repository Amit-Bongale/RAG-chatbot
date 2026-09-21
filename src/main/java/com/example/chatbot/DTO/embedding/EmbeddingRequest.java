package com.example.chatbot.DTO.embedding;

public record EmbeddingRequest(
        String model,
        String input
) {
}
