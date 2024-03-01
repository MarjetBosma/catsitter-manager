package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;

import java.util.List;

public interface InvoiceService {

    List<InvoiceDto> getAllInvoices();

    InvoiceDto getInvoice(long noToFind);

    InvoiceDto createInvoice(InvoiceInputDto invoiceInputDto);

    InvoiceDto editInvoice(long noToEdit, InvoiceInputDto invoiceInputDto);

    long deleteInvoice(long noToDelete);
}
