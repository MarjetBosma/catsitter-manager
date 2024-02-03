package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
