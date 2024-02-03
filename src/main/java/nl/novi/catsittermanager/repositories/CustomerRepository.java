package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
