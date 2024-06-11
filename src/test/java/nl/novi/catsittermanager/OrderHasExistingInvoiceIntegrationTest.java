package nl.novi.catsittermanager;

import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.controllers.ExceptionController;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
@Import({ExceptionController.class, TestConfig.class})
public class OrderHasExistingInvoiceIntegrationTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    // todo: Geeft bij assertTrue false. Bij debuggen wordt de order wel gezet en opgeslagen en service unittest op methode existsByOrder slaagt wel, dus ik snap niet goed wat hier gebeurt.

    @Test
    void testHasExistingInvoice() {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        Order order = new Order();
        order.setOrderNo(orderNo);
        orderRepository.save(order);

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoiceRepository.save(invoice);

        // Act
        boolean exists = orderService.hasExistingInvoice(orderNo);

        // Assert
        assertTrue(exists);
    }

//    @Test
//    void testHasNoExistingInvoice() {
//
//        // Arrange
//        UUID orderNo = UUID.randomUUID();
//
//        Order order = new Order();
//        order.setOrderNo(orderNo);
//        orderRepository.save(order);
//
//        // Act
//        boolean hasInvoice = orderService.hasExistingInvoice(orderNo);
//
//        // Assert
//        assertFalse(hasInvoice);
//    }
}