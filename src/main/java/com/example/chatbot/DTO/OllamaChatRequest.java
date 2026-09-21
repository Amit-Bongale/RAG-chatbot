package com.example.chatbot.DTO;

import java.util.List;

//for request with memory
public record OllamaChatRequest(
        String model,
        List<Message> messages,
        boolean stream
) {
}
