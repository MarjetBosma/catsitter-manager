package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Customer getByUsername(String Username);
}
