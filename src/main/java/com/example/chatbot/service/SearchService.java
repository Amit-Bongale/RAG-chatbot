package com.example.chatbot.service;

import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import com.example.chatbot.DTO.vectorDb.SearchResult;
import com.example.chatbot.util.SimilarityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    private static final double MIN_SCORE = 0.7;

    public List<SearchResult> search(String query){

        List<Double> queryEmbedding = embeddingService.generateEmbedding(query);

        return vectorStoreService.getDocuments()
                .stream()
                .map(doc -> new SearchResult (doc, SimilarityUtil.cosineSimilarity(
                        doc.embeddings(), queryEmbedding ))
                )
                .filter(searchResult -> searchResult.score() >= MIN_SCORE)
                .sorted((a,b) -> Double.compare(
                        b.score() , a.score()
                ))
                .limit(3)
                .toList();
    }

    // search using cosineSimilarity and return relevant documents chunk
    public String buildContext(String query){
        List<SearchResult> documents = search(query);

        if (documents.isEmpty()){
            return "";
        }

        documents.forEach(doc -> System.out.println("Score: " + doc.score() + " content:" + doc.chunk()));

        return documents.stream()
                .map(result -> result.chunk().content())
                .collect(Collectors.joining("\n\n"));

    }

}
