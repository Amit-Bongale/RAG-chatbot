package com.example.chatbot.controller;

import com.example.chatbot.DTO.ChatRequest;
import com.example.chatbot.DTO.ChatResponse;
import com.example.chatbot.DTO.Message;
import com.example.chatbot.DTO.Vehicle;
import com.example.chatbot.service.ChatMemoryService;
import com.example.chatbot.service.OllamaService;
import com.example.chatbot.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OllamaService ollamaService;
    private final ChatMemoryService memoryService;
    private final VehicleService vehicleService;

    @PostMapping("/query")
    public ChatResponse query(
            @RequestBody ChatRequest request
            ){
        String res = ollamaService.ask(request.message());

        return new ChatResponse(res);
    }

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request
    ){
        //load old message of user by session id
        List<Message> messages = memoryService.getRecentMessages(request.sessionId());

        //add the current query to message
        messages.add(new Message("user" , request.message()));

        String res = ollamaService.generate(messages);

        //add ai response to memory
        messages.add(new Message("assistant" , res));

        return new ChatResponse(res);
    }


    @PostMapping("/vehicle")
    public ChatResponse vehicle(
            @RequestBody ChatRequest request
    ){
        //load old message of user by session id
        List<Message> messages = memoryService.getRecentMessages(request.sessionId());

        //add the current query to message
        messages.add(new Message("user" , request.message()));

        boolean vehicleQuery = request.message().contains("bike")
                || request.message().contains("vehicle")
                || request.message().contains("rent")
                || request.message().contains("price");

        if (vehicleQuery){
            List<Vehicle> vehicles = vehicleService.getVehicles();

            String inventory = vehicles.stream().map(
                    vehicle -> String.format(
                            "%s - %s - Rs%.0f/day",
                            vehicle.name(),
                            vehicle.city(),
                            vehicle.pricePerDay()
                    )
            ).collect(Collectors.joining("\n"));

            messages.add(
                    new Message("system" , """
                    Available Vehicles:
                    
                    %s
                    
                    Use ONLY these vehicles while answering.
                    Do not invent vehicles.
                    """.formatted(inventory))
            );
        }

        String res = ollamaService.generate(messages);

        //add ai response to memory
        messages.add(new Message("assistant" , res));

        return new ChatResponse(res);
    }



}
