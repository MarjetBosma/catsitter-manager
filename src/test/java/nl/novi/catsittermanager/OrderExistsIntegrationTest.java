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

    @Test
    void testExistsByOrder_OrderNo() {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        Order order = new Order();
        order.setOrderNo(orderNo);
        orderRepository.save(order);

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoiceRepository.save(invoice);

        // Act
        boolean exists = invoiceRepository.existsByOrder_OrderNo(orderNo);

        // Assert
        assertTrue(exists);
    }
}