package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.InvoiceRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceMapperTest {

    @Test
    void testInvoiceToinvoiceResponse() {

        // Arrange
        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );
        Order order = OrderFactory.randomOrder(tasks).build();
        Invoice invoice = InvoiceFactory.randomInvoice().order(order).build();

        // Act
        InvoiceResponse invoiceResponse = InvoiceMapper.InvoiceToInvoiceResponse(invoice);

        // Assert
        assertEquals(invoice.getInvoiceNo(), invoiceResponse.invoiceNo());
        assertEquals(invoice.getInvoiceDate().toString(), invoiceResponse.invoiceDate());
        assertEquals(invoice.getAmount(), invoiceResponse.amount());
        assertEquals(invoice.getPaid(), invoiceResponse.paid());
        assertEquals(invoice.getOrder().getOrderNo(), invoiceResponse.orderNo());
    }

    @Test
    void testInvoiceRequestToInvoice() {

        // Arrange
        InvoiceRequest invoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order order = OrderFactory.randomOrder(tasks).build();

        order.setOrderNo(invoiceRequest.orderNo());
        order.setStartDate(LocalDate.now());
        order.setEndDate(LocalDate.now().plusDays(5));
        order.setDailyNumberOfVisits(2);
        order.setTasks(tasks);

        double expectedAmount = order.calculateTotalCost();

        // Print statements for debugging
        System.out.println("Order Details:");
        System.out.println("Order No: " + order.getOrderNo());
        System.out.println("Start Date: " + order.getStartDate());
        System.out.println("End Date: " + order.getEndDate());
        System.out.println("Daily Number of Visits: " + order.getDailyNumberOfVisits());
        System.out.println("Total Number of Visits: " + order.calculateTotalNumberOfVisits());
        System.out.println("Tasks: " + order.getTasks());
        System.out.println("Expected Amount: " + expectedAmount);

        // Act
        Invoice invoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest);
        invoice.setOrder(order);

        // Print statements for debugging
        System.out.println("Invoice Details:");
        System.out.println("Invoice No: " + invoice.getInvoiceNo());
        System.out.println("Invoice Date: " + invoice.getInvoiceDate());
        System.out.println("Amount: " + invoice.getAmount());
        System.out.println("Paid: " + invoice.getPaid());
        System.out.println("Order No in Invoice: " + invoice.getOrder().getOrderNo());

        // Assert
        assertEquals(invoiceRequest.invoiceDate(), invoice.getInvoiceDate().toString());
        assertEquals(expectedAmount, invoice.getAmount());
        assertEquals(invoiceRequest.paid(), invoice.getPaid());
        assertEquals(invoiceRequest.orderNo(), invoice.getOrder().getOrderNo());
        assertEquals(tasks, invoice.getOrder().getTasks());
    }
}