package com.example.chatbot.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.chatbot.DTO.Message;
import com.example.chatbot.DTO.OllamaChatRequest;
import com.example.chatbot.DTO.OllamaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import javax.swing.*;
import java.util.List;

@Service

//constructor injection
@RequiredArgsConstructor
public class OllamaService {

    private final RestClient restClient;

    @Value("${ollama.model}")
    private String model;

    //for single chat
    public String ask(String userPrompt){

        String prompt = """
            You are an AI rental assistant.
    
            You help users choose vehicles.
    
            Be concise and helpful.
    
            User Question:
            %s
            """.formatted(userPrompt);

        OllamaRequest request = new OllamaRequest(model , prompt , false);

        JsonNode response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new RuntimeException("response field not found");
        }
        return  response.get("response").asString();

    }

    //for chat with memory
    public String generate(List<Message> messages){

        System.out.println("Sending messages: " + messages);
        OllamaChatRequest request = new OllamaChatRequest(model , messages , false);

        JsonNode response = restClient.post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new RuntimeException("response field not found");
        }

        return response.get("message").get("content").asString();
    }

}
