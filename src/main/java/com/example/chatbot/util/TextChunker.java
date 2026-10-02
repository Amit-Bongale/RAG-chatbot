package com.example.chatbot.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP = 50;

    public List<String> chunk(String text){

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()){
            int end = Math.min(start + CHUNK_SIZE, text.length());

            String chunk = text.substring(start , end).trim();

            if(!chunk.isBlank()){
                chunks.add(chunk);
            }

            if(end == text.length()){
                break;
            }

            start = end - OVERLAP;
        }

        return chunks;
    }
}
