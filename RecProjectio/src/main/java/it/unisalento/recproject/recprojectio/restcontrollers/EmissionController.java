package it.unisalento.recproject.recprojectio.restcontrollers;

import it.unisalento.recproject.recprojectio.service.EmissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Add a base path to all endpoints
public class EmissionController {

    private final EmissionService emissionService;

    public EmissionController(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    @GetMapping("/calculate-emissions")
    public double calculateEmissions(@RequestParam double energyConsumedInKWh) {
        return emissionService.calculateEmissions(energyConsumedInKWh);
    }
}