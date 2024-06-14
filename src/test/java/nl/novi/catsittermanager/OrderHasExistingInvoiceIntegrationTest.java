package nl.novi.catsittermanager;

import jakarta.persistence.EntityManager;
import nl.novi.catsittermanager.config.TestConfig;
import nl.novi.catsittermanager.controllers.ExceptionController;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.String.valueOf;

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

    @Autowired
    private EntityManager entityManager;

    //  todo: Deze test heb ik geschreven omdat een van de testen in de InvoiceControllerTest (givenExistingInvoiceForGivenOrder_whenCreateInvoice_thenConflictShouldBeReturned())e en het probleem lijkt te zitten in de hasExistngInvoice) methode die ik hier test. Op het niveau van unittests gaat alles goed, en ook in Postman werkt deze check correct. Maar bij de WebMvc test  werkt het niet, en ook deze SpingBoot test krijg ik niet slagend. Er lijkt een probleem te zijn met de simulatie van de database.

    @Test
    void testHasExistingInvoice() {

        // Arrange
        UUID orderNo = UUID.randomUUID();
        System.out.println("orderNo after initialization: " + orderNo); // as expected
        Order order = new Order();
        order.setOrderNo(orderNo);
        System.out.println("orderNo after setting to new order " + order.getOrderNo()); // as expected
        orderRepository.save(order);
        System.out.println("orderNo after saving order to repository: " + order.getOrderNo()); // as expected

        UUID invoiceNo = UUID.randomUUID();
        System.out.println("invoiceNo after initialization: " + invoiceNo); // as expected

        Invoice invoice = new Invoice();
        invoice.setInvoiceNo(invoiceNo);
        System.out.println("invoiceNo after setting to new invoice: " + invoice.getInvoiceNo()); // as expected
        invoice.setOrder(order);
        order.setInvoice(invoice);
        System.out.println("orderNo after setting it to the invoice: " + invoice.getOrder().getOrderNo()); // as expected
        System.out.println("invoiceNo after setting it to the order:" + order.getInvoice().getInvoiceNo());
        invoiceRepository.save(invoice);

        System.out.println("orderNo after saving invoice to repository: " + invoice.getOrder().getOrderNo()); // as expected
        System.out.println("invoiceNo at order after saving invoice to repository: " + order.getInvoice().getInvoiceNo()); // as expected

        entityManager.flush();
        entityManager.clear();

        // Act
        boolean exists = orderService.hasExistingInvoice(orderNo);
        System.out.println(valueOf(exists));  // false
        // Assert
        Assertions.assertTrue(exists);
    }

    @Test
    void testHasNoExistingInvoice() {

        // Arrange
        UUID orderNo = UUID.randomUUID();

        Order order = new Order();
        order.setOrderNo(orderNo);
        orderRepository.save(order);

        // Act
        boolean hasInvoice = orderService.hasExistingInvoice(orderNo);

        entityManager.flush();
        entityManager.clear();

        // Assert
        Assertions.assertFalse(hasInvoice);
    }
}