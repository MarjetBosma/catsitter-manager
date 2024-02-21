package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.models.Invoice;

public class InvoiceMapper {

    public InvoiceDto TransferToDto(Invoice invoice) {

        InvoiceDto invoiceDto = new InvoiceDto();

        invoiceDto.invoiceNo = invoice.getInvoiceNo();
        invoiceDto.invoiceDate = invoice.getInvoiceDate();
        invoiceDto.amount = invoice.getAmount();
        invoiceDto.order = invoice.getOrder();

        return invoiceDto;
    }

    public Invoice TransferToInvoice(InvoiceInputDto invoiceDto) {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNo(invoiceDto.invoiceNo);
        invoice.setInvoiceDate(invoiceDto.invoiceDate);
        invoice.setAmount(invoiceDto.amount);
        invoice.setOrder(invoiceDto.order);

        return invoice;
    }
}
