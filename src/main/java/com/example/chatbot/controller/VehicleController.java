package com.example.chatbot.controller;

import com.example.chatbot.DTO.Vehicle;
import com.example.chatbot.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/all")
    public List<Vehicle> getVehicles(){
        return  vehicleService.getVehicles();
    }
}
