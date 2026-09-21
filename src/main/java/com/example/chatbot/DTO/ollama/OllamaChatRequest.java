package com.example.chatbot.DTO.ollama;

import com.example.chatbot.DTO.Message;

import java.util.List;

//for request with memory
public record OllamaChatRequest(
        String model,
        List<Message> messages,
        boolean stream
) {
}
