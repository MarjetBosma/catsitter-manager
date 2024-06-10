package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.InvoiceRequestFactory;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.InvoiceFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceMapperTest {

    @Test
    void testInvoiceToinvoiceResponse() {

        // Arrange
        Invoice invoice = InvoiceFactory.randomInvoice().build();

        // Act
        InvoiceResponse invoiceResponse = InvoiceMapper.InvoiceToInvoiceResponse(invoice);

        // Assert
        assertEquals(invoice.getInvoiceNo(), invoiceResponse.invoiceNo());
        assertEquals(invoice.getInvoiceDate().toString(), invoiceResponse.invoiceDate());
        assertEquals(invoice.getAmount(), invoiceResponse.amount());
        assertEquals(invoice.getPaid(), invoiceResponse.paid());
        assertEquals(invoice.getOrder().getOrderNo(), invoiceResponse.orderNo());
    }

    @Test void testInvoiceRequestToInvoice() {

        // Arrange
        InvoiceRequest invoiceRequest = InvoiceRequestFactory.randomInvoiceRequest().build();

        // Act
        Invoice invoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest);

        // Assert
        assertEquals(invoiceRequest.invoiceDate(), invoice.getInvoiceDate().toString());
        assertEquals(invoiceRequest.amount(), invoice.getAmount());
        assertEquals(invoiceRequest.paid(), invoice.getPaid());
        assertEquals(invoiceRequest.orderNo(), invoice.getOrder().getOrderNo());
    }
}