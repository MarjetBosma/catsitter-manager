package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.models.Invoice;

public class InvoiceMapper {

    public static InvoiceDto transferToDto(Invoice invoice) {
        return new InvoiceDto(invoice.getInvoiceNo(),
                              invoice.getInvoiceDate(),
                              invoice.getAmount(),
                              invoice.getPaid(),
                              invoice.getOrder()
        );
    }

    public static Invoice transferFromDto(InvoiceInputDto invoiceInputDto) {
        return new Invoice(invoiceInputDto.invoiceNo(), // In een latere fase deze hier niet meegeven, maar automatisch via database
                           invoiceInputDto.invoiceDate(),
                           invoiceInputDto.amount(),
                           invoiceInputDto.paid(),
                           invoiceInputDto.order()
        );
    }
}
