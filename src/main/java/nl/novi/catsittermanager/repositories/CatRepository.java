package nl.novi.catsittermanager.repositories;

import io.micrometer.observation.ObservationFilter;
import nl.novi.catsittermanager.models.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CatRepository extends JpaRepository<Cat, UUID> {
}
