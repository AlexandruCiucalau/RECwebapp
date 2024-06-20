package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.Credit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CreditRepository extends MongoRepository<Credit, String> {
    Optional<Credit> findByUserId(String userId);
}
