package com.example.chatbot.Runner;

import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import com.example.chatbot.service.EmbeddingService;
import com.example.chatbot.service.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    @Override
    public void run(String... args) throws Exception {
        addToVectorStore("1" , "Apache RTR 160 is available in Bangalore for ₹800 per day.");
        addToVectorStore("2" , " Xpulse 200 is suitable for adventure touring.");
        addToVectorStore("3" , "Bookings can be cancelled up to 24 hours before pickup.");
        addToVectorStore("4" , "Customers receive a full refund for eligible cancellations");
    }

    public void addToVectorStore(String id, String content){
        List<Double> embedding = embeddingService.generateEmbedding(content);
        vectorStoreService.addDocument( new DocumentChunk(
                id , content , embedding
        ));
    }
}
