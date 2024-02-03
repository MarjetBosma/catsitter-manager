package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.CustomerCatSitter;
import nl.novi.catsittermanager.models.CustomerCatSitterKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCatSitterRepository extends JpaRepository<CustomerCatSitter, CustomerCatSitterKey> {
}
