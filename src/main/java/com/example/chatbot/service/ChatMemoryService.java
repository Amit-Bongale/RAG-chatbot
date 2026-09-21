package com.example.chatbot.service;

import com.example.chatbot.DTO.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatMemoryService {

    // stores key:userID/sessionId, value: list of messages
    private final Map<String , List<Message>> conversation = new ConcurrentHashMap<>();

    //stores the summary of the user old conversation
    private final Map<String , String> summaries = new ConcurrentHashMap<>();

    private static final int MAX_MESSAGE = 8;
    private static final int SUMMARY_THRESHOLD = 20;
    private static final int WINDOW_SIZE = 30;

    private static final Message  SYSTEM_PROMPT =
            new Message(
                    "system",
                    """
                    You are an AI rental assistant.
    
                    Always answer in English.
    
                    Help users choose vehicles.
    
                    Be concise and helpful.
                    """
            );


    public List<Message> getMessages(String sessionId){
        return conversation.computeIfAbsent(sessionId , k -> {
            List<Message> message = new ArrayList<>();
            message.add(
                    new Message(
                            "system",
                            """
                            You are a vehicle rental support executive.
                            Speak like a real customer support agent.
                            Keep answers under 2 sentences.
                            Never explain your reasoning.
                    
                            Never mention:
                            - context
                            - knowledge
                            - database
                            - retrieved information
                            - AI
                    
                            Answer directly and confidently only in English.
                            """
                    )
            );

            return  message;
        });
    }

    public void addMessage(String sessionId, Message message){
        getMessages(sessionId).add(message);
    }

    public String getSummary(String sessionId){
        return summaries.getOrDefault(sessionId , "");
    }

    public void updateSummary(String sessionId, String summary){
        summaries.put(sessionId , summary);
    }

    // get latest 20 messages [sliding window]
    public List<Message> getRecentMessages(String sessionId){
        List<Message> messages = getMessages(sessionId);

        if (messages.size() <= MAX_MESSAGE ){
            return messages;
        }

        // return last messages from the map
        return messages.subList(
                messages.size() - MAX_MESSAGE, messages.size()
        );
    }

    // store only recent messages and reduce memory [after summary generation]
    public void compactMemory(String sessionId){
        List<Message> messages = getMessages(sessionId);

        if(messages == null || messages.size() <= WINDOW_SIZE){
            return;
        }

        List<Message> recentMessages = new ArrayList<>(
                messages.subList( messages.size() - WINDOW_SIZE , messages.size())
        );

        conversation.put(sessionId , recentMessages);
    }


    public boolean needsSummary(String sessionId){
        return getMessages(sessionId).size() > SUMMARY_THRESHOLD;
    }

    //build context for chat
    public List<Message> buildContext(String sessionId){
        List<Message> context = new ArrayList<>();

        context.add(SYSTEM_PROMPT);

        String summary = getSummary(sessionId);

        if (summary != null && !summary.isBlank()){
            context.add( new Message(
                    "system",
                    """
                     The following is a summary of
                     previous conversations with the user.
                     Use it as historical context.

                     Summary:
                       %s
                     """.formatted(summary)
            ));
        }

        context.addAll(getRecentMessages(sessionId));

        return context;
    }

}
