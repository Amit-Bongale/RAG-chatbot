package com.example.chatbot.Interface;

public interface Tool {
    boolean supports (String query);
    String executes (String query);
}
