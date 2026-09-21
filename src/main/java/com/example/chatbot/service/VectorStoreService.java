package com.example.chatbot.service;

import com.example.chatbot.DTO.vectorDb.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VectorStoreService {

    private final List<DocumentChunk> documents = new ArrayList<>();

    public void addDocument(DocumentChunk doc){
        documents.add(doc);
    }

    public List<DocumentChunk> getDocuments(){
        return  documents;
    }
}
