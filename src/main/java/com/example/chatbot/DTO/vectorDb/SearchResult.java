package com.example.chatbot.DTO.vectorDb;

public record SearchResult(
        DocumentChunk chunk,
        //Store cosine-similarity
        double score
) {
}
