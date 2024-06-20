package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RewardRepository extends MongoRepository<Reward, String> {
    List<Reward> findByUserId(String userId);
}