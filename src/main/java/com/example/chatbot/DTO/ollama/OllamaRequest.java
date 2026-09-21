package com.example.chatbot.DTO.ollama;

//for single requests
public record OllamaRequest(
        String model,
        String prompt,
        boolean stream
) {
}
