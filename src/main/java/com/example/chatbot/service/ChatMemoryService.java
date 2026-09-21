package com.example.chatbot.service;

import com.example.chatbot.DTO.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatMemoryService {

    // stores key:userID/sessionId, value: list of messages
    private final Map<String , List<Message>> conversation = new ConcurrentHashMap<>();

    public List<Message> getMessages(String sessionId){
        return conversation.computeIfAbsent(sessionId , k -> {
            List<Message> message = new ArrayList<>();
            message.add(
                    new Message(
                            "system",
                            """
                            You are an AI assistant.
                            Always respond in English.
                            Never respond in Chinese.
                            Be concise and helpful.
                            """
                    )
            );

            return  message;
        });
    }

    public void addMessage(String sessionId, Message message){
        getMessages(sessionId).add(message);
    }
}
