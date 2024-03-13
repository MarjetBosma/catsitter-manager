package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.Catsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CatsitterRepository extends JpaRepository<Catsitter, UUID> {
    Catsitter getByUsername(String Username);
}
