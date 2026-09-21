package com.example.chatbot.DTO;

//for single requests
public record OllamaRequest(
        String model,
        String prompt,
        boolean stream
) {
}
