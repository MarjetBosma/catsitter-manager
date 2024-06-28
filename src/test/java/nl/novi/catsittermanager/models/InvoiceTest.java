package nl.novi.catsittermanager.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InvoiceTest {

    private Invoice invoice;
    private Order order;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        order = mock(Order.class);
    }

    @Test
    void testCalculateAmount_prePersist() {
        // Arrange
        when(order.calculateTotalCost()).thenReturn(150.0);
        invoice.setOrder(order);

        // Act
        invoice.calculateAmount();

        // Assert
        assertEquals(150.0, invoice.getAmount());
    }

    @Test
    void testCalculateAmount_preUpdate() {
        // Arrange
        when(order.calculateTotalCost()).thenReturn(200.0);
        invoice.setOrder(order);

        // Act
        invoice.calculateAmount();

        // Assert
        assertEquals(200.0, invoice.getAmount());
    }

    @Test
    void testConstructorAndGetters() {
        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        LocalDate invoiceDate = LocalDate.now();
        Double amount = 100.0;
        Boolean paid = true;
        Order order = mock(Order.class);

        // Act
        Invoice invoice = new Invoice(invoiceNo, invoiceDate, amount, paid, order);

        // Assert
        assertEquals(invoiceNo, invoice.getInvoiceNo());
        assertEquals(invoiceDate, invoice.getInvoiceDate());
        assertEquals(amount, invoice.getAmount());
        assertEquals(paid, invoice.getPaid());
        assertEquals(order, invoice.getOrder());
    }

    @Test
    void testBuilder() {
        // Arrange
        UUID invoiceNo = UUID.randomUUID();
        LocalDate invoiceDate = LocalDate.now();
        Double amount = 100.0;
        Boolean paid = true;
        Order order = mock(Order.class);

        // Act
        Invoice invoice = Invoice.builder()
                .invoiceNo(invoiceNo)
                .invoiceDate(invoiceDate)
                .amount(amount)
                .paid(paid)
                .order(order)
                .build();

        // Assert
        assertEquals(invoiceNo, invoice.getInvoiceNo());
        assertEquals(invoiceDate, invoice.getInvoiceDate());
        assertEquals(amount, invoice.getAmount());
        assertEquals(paid, invoice.getPaid());
        assertEquals(order, invoice.getOrder());
    }
}