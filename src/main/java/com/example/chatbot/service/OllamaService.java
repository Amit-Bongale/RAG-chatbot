package com.example.chatbot.service;

import com.example.chatbot.DTO.Message;
import com.example.chatbot.DTO.ollama.OllamaChatRequest;
import com.example.chatbot.DTO.ollama.OllamaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // generate the summary of the previous chats of the user
    public String summarize(List<Message> messages){

        messages.add( new Message("system" , "Summarize the following conversation\n" +
                "in 5 bullet points."));

        String conversation = messages.stream().map(
                message -> "%s: %s".formatted(message.role() , message.content())
        ).collect(Collectors.joining("\n"));

        String prompt = """
            Summarize the following conversation.

            Rules:
            - Keep important user preferences.
            - Keep important decisions.
            - Keep important context.
            - Maximum 5 bullet points.
            - Ignore greetings and small talk.

            Conversation:
            %s
            """.formatted(conversation);

        OllamaRequest request = new OllamaRequest(model , prompt , false);

        JsonNode response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new RuntimeException("response field not found");
        }

        return response.get("response").asString();
    }


    public String askWithContext(List<Message> messages, String knowledge){
        List<Message> content = new ArrayList<>(messages);

        System.out.println("knowledge: " + knowledge);

        content.add(1, new Message(
                "system",
                """
                You are a vehicle rental assistant.
                Answer ONLY using the provided knowledge.

                If the knowledge contains the answer,
                respond directly from the knowledge.

                Do NOT say "I don't know" if the answer is present.

                Do NOT use outside knowledge.

                Knowledge:
                %s
                """.formatted(knowledge)
        ));

        return generate(content);

    }

}