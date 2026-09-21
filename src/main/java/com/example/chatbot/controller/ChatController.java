package com.example.chatbot.controller;

import com.example.chatbot.DTO.ChatRequest;
import com.example.chatbot.DTO.ChatResponse;
import com.example.chatbot.DTO.Message;
import com.example.chatbot.service.ChatMemoryService;
import com.example.chatbot.service.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OllamaService ollamaService;
    private final ChatMemoryService memoryService;

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request
            ){
        String res = ollamaService.ask(request.message());

        return new ChatResponse(res);
    }

    @PostMapping("/memory")
    public ChatResponse chat2(
            @RequestBody ChatRequest request
    ){
        //load old message of user by session id
        List<Message> messages = memoryService.getMessages(request.sessionId());

        //keep only last 20 conversation [sliding window]
        if(messages.size() > 20){
            messages = messages.subList(
                    messages.size() - 20,
                    messages.size()
            );
        }

        //add the current query to message
        messages.add(new Message("user" , request.message()));

        String res = ollamaService.generate(messages);

        //add ai response to memory
        messages.add(new Message("assistant" , res));

        return new ChatResponse(res);
    }
}
