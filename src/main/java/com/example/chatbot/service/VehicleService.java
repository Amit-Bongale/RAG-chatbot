package com.example.chatbot.service;

import com.example.chatbot.DTO.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    public List<Vehicle> getVehicles(){
        return  List.of(
                new Vehicle(1L, " Apache rtr 310", "Bengaluru", 800.0),
                new Vehicle(2L, " Xpulse 200", "Davangere", 600.0),
                new Vehicle(4L, " Apache rtr 400", "Bengaluru", 900.0),
                new Vehicle(5L, " Activa 310", "Davangere", 300.0)
                );
    }
}
