package nl.novi.catsittermanager;

import jakarta.transaction.Transactional;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.controllers.ExceptionController;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
@Import({ExceptionController.class, TestConfig.class})
public class OrderExistsIntegrationTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    // todo: Geeft bij assertTrue false. Bij debuggen wordt de order wel gezet en opgeslagen en service unittest op methode existsByOrder slaagt wel, dus ik snap niet goed wat hier gebeurt.

    @Test
    void testExistsByOrder_OrderNo() {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        Order order = new Order();
        order.setOrderNo(orderNo);
        orderRepository.saveAndFlush(order);

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoiceRepository.saveAndFlush(invoice);

        // Act
        boolean exists = invoiceRepository.existsByOrder_OrderNo(orderNo);

        // Assert
        assertTrue(exists);
    }
}