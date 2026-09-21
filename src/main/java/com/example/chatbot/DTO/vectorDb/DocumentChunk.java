package com.example.chatbot.DTO.vectorDb;

import java.util.List;

public record DocumentChunk(
        String id,
        String content,
        List<Double> embeddings
) {
}
