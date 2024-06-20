package it.unisalento.recproject.recprojectio.repositories;
import it.unisalento.recproject.recprojectio.domain.RenewableEnergy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RenewableEnergyRepository extends MongoRepository<RenewableEnergy, String> {
    List<RenewableEnergy> findByUserId(String userId);
}