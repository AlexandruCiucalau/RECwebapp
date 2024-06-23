package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);
}