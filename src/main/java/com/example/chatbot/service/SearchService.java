package com.example.chatbot.service;

import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import com.example.chatbot.util.SimilarityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public List<DocumentChunk> search(String query){

        List<Double> queryEmbedding = embeddingService.generateEmbedding(query);

        return vectorStoreService.getDocuments()
                .stream().sorted((a,b) -> Double.compare(
                        SimilarityUtil.cosineSimilarity(b.embeddings() , queryEmbedding),
                        SimilarityUtil.cosineSimilarity(a.embeddings(), queryEmbedding)
                ))
                .limit(3)
                .toList();
    }

    // search using cosineSimilarity and return relevant documents chunk
    public String buildContext(String query){
        List<DocumentChunk> documents = search(query);
        return documents.stream().map(DocumentChunk::content)
                .collect(Collectors.joining("\n\n"));

    }

}
