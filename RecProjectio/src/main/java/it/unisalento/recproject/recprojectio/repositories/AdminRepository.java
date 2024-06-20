package it.unisalento.recproject.recprojectio.repositories;

import it.unisalento.recproject.recprojectio.domain.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByEmail(String email);
}