package it.unisalento.recproject.recprojectio.service;

import it.unisalento.recproject.recprojectio.domain.RenewableEnergy;
import it.unisalento.recproject.recprojectio.repositories.RenewableEnergyRepository;
import it.unisalento.recproject.recprojectio.di.SolarPanel;
import it.unisalento.recproject.recprojectio.di.WindTurbine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RenewableEnergyService {

    @Autowired
    private RenewableEnergyRepository renewableEnergyRepository;

    public List<RenewableEnergy> getRenewableSourcesByUserId(String userId) {
        return renewableEnergyRepository.findByUserId(userId);
    }

    public RenewableEnergy addRenewableSource(RenewableEnergy renewableEnergy) {
        return renewableEnergyRepository.save(renewableEnergy);
    }

    public Optional<RenewableEnergy> getRenewableSourceById(String id) {
        return renewableEnergyRepository.findById(id);
    }

    public RenewableEnergy generateEnergy(String id) {
        Optional<RenewableEnergy> renewableEnergyOpt = renewableEnergyRepository.findById(id);
        if (!renewableEnergyOpt.isPresent()) {
            throw new RuntimeException("Renewable energy source not found");
        }

        RenewableEnergy renewableEnergy = renewableEnergyOpt.get();
        double generatedEnergy;
        if ("SolarPanel".equals(renewableEnergy.getType())) {
            SolarPanel solarPanel = new SolarPanel(0.2, 10); // Example values
            generatedEnergy = solarPanel.generateEnergy();
        } else if ("WindTurbine".equals(renewableEnergy.getType())) {
            WindTurbine windTurbine = new WindTurbine(5); // Example values
            generatedEnergy = windTurbine.generateEnergy();
        } else {
            throw new RuntimeException("Unknown renewable energy type");
        }

        renewableEnergy.setGeneratedEnergy(renewableEnergy.getGeneratedEnergy() + generatedEnergy);
        return renewableEnergyRepository.save(renewableEnergy);
    }
    @Scheduled(fixedRate = 60000) // Schedule to run every 60 seconds
    public void generateEnergyForAllSources() {
        List<RenewableEnergy> sources = renewableEnergyRepository.findAll();
        for (RenewableEnergy source : sources) {
            generateEnergy(source.getId());
        }
    }
}