package com.example.chatbot.Tools;

import com.example.chatbot.Interface.Tool;
import com.example.chatbot.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleTool implements Tool {

    private final VehicleService vehicleService;

    @Override
    public boolean supports(String query) {
        query = query.toLowerCase();

        return query.contains("vehicle")
                || query.contains("bike")
                || query.contains("rent");
    }

    @Override
    public String executes(String query) {
        return vehicleService.getVehicles().stream()
                .map(v ->
                    "%s | %s | Rs %.0f/day".formatted(
                            v.name() ,
                            v.city(),
                            v.pricePerDay()
                    )
                ).collect(Collectors.joining("\n"));
    }
}
