package com.example.chatbot.service;

import com.example.chatbot.DTO.embedding.EmbeddingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final RestClient restClient;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    public List<Double> generateEmbedding(String text){
        EmbeddingRequest request = new EmbeddingRequest( embeddingModel, text);

        JsonNode response = restClient.post()
                .uri("/api/embed")
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        if (response == null){
            throw new RuntimeException("Embedding response is null");
        }

        JsonNode embeddingNode =  response.get("embeddings").get(0);

        List<Double> embedding = new ArrayList<>();
        for (JsonNode value : embeddingNode){
            embedding.add(value.asDouble());
        }

        return embedding;
    }
}
