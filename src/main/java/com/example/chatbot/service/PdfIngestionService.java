package com.example.chatbot.service;

import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import com.example.chatbot.util.TextChunker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfIngestionService {

    private final PdfService pdfService;
    private final TextChunker textChunker;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    public int ingestPdf(byte[] pdfBytes) throws IOException{

        //extract text from pdf
        String text = pdfService.extractText(pdfBytes);

        //convert text as chunks
        List<String> chunks = textChunker.chunk(text);

        //generate embeddings and store it  in vector store
        for(String chunk : chunks){
            List<Double> embeddings = embeddingService.generateEmbedding(chunk);

            DocumentChunk documentChunk = new DocumentChunk(
                    UUID.randomUUID().toString(),
                    chunk,
                    embeddings
            );

            vectorStoreService.addDocument(documentChunk);
        }

        return chunks.size();

    }
}
