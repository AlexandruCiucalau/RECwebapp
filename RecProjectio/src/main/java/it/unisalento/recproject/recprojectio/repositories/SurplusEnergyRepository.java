package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.SurplusEnergy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
public interface SurplusEnergyRepository extends MongoRepository<SurplusEnergy, String> {
    List<SurplusEnergy> findByDate(LocalDate date);
    List<SurplusEnergy> findByUserId(String userId);
    List<SurplusEnergy> findByUserIdAndDate(String userId, LocalDate date);
}
