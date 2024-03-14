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
        return Invoice.builder().invoiceNo(invoiceInputDto.invoiceNo())
                .invoiceDate(invoiceInputDto.invoiceDate())
                .amount(invoiceInputDto.amount())
                .paid(invoiceInputDto.paid())
                .order(invoiceInputDto.order())
                .build();
    }
}
