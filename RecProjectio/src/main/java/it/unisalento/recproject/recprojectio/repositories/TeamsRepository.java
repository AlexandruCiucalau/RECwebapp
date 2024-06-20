package it.unisalento.recproject.recprojectio.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import it.unisalento.recproject.recprojectio.domain.Team;
import java.util.Optional;
import java.util.List;
@Repository
public interface TeamsRepository extends MongoRepository<Team, String> {
    List<Team> findByName(String name);
}
