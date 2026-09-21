package com.example.chatbot.controller;

import com.example.chatbot.DTO.embedding.TextRequest;
import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import com.example.chatbot.DTO.vectorDb.SearchResult;
import com.example.chatbot.service.EmbeddingService;
import com.example.chatbot.service.SearchService;
import com.example.chatbot.util.SimilarityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/embed")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;
    private final SearchService searchService;

    @PostMapping
    public Map<String , Object> embed(@RequestBody TextRequest request){
        List<Double> embedding = embeddingService.generateEmbedding(request.text());

        return Map.of("dimensions" , embedding.size());
    }

    @GetMapping("/test")
    public Map<String , Object> similarity(){

        List<Double> e1 = embeddingService.generateEmbedding("bike rental");
        List<Double> e2 = embeddingService.generateEmbedding("rent a motorCycle");
        List<Double> e3 = embeddingService.generateEmbedding("pizza delivery");

        double s1 = SimilarityUtil.cosineSimilarity(e1 , e2);
        double s2 = SimilarityUtil.cosineSimilarity(e1 , e3);

        return Map.of(
                "Bike vs motorcycle", s1,
                "Bike vs delivery" , s2
        );

    }

    @PostMapping("/search")
    public List<SearchResult> search(@RequestBody TextRequest request){
        return searchService.search(request.text());
    }

}
