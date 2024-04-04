package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.models.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public static InvoiceResponse InvoiceToInvoiceResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getInvoiceNo(),
                invoice.getInvoiceDate(),
                invoice.getAmount(),
                invoice.getPaid(),
                invoice.getOrder()
        );
    }

    public static Invoice InvoiceRequestToInvoice(InvoiceRequest invoiceRequest) {
        return Invoice.builder()
                .invoiceDate(invoiceRequest.invoiceDate())
                .amount(invoiceRequest.amount())
                .paid(invoiceRequest.paid())
                .build();
    }
}
