package com.example.chatbot.DTO.embedding;

import java.util.List;

public record EmbeddingResponse(
        List<Double> embedding
) {
}
