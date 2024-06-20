package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.EnergyConsumption;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface EnergyConsumptionRepository extends MongoRepository<EnergyConsumption, String> {
    List<EnergyConsumption> findByDate(LocalDate date);
    List<EnergyConsumption> findByUserId(String userId);
    List<EnergyConsumption> findByUserIdAndDate(String userId, LocalDate date);
}
