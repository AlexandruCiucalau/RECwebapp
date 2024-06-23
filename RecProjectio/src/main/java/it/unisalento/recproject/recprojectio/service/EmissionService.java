package it.unisalento.recproject.recprojectio.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmissionService {

    @Value("${climatiq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EmissionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double calculateEmissions(double energyConsumedInKWh) {
        String url = "https://beta3.api.climatiq.io/estimate";
        Map<String, Object> request = new HashMap<>();
        request.put("emission_factor", Map.of("activity_id", "electricity-energy-source_grid_mix"));
        request.put("parameters", Map.of("energy", energyConsumedInKWh, "energy_unit", "kWh"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class, headers);
            if (response != null && response.containsKey("co2e")) {
                return (double) response.get("co2e");
            } else {
                throw new RuntimeException("Failed to calculate emissions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling Climatiq API", e);
        }
    }
}