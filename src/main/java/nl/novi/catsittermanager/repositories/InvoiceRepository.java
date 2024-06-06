package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    boolean existsByOrder_OrderNo(UUID orderNo);
}
