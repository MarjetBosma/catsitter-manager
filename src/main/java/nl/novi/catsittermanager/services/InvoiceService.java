package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    List<InvoiceDto> getAllInvoices();

    InvoiceDto getInvoice(UUID noToFind);

    InvoiceDto createInvoice(InvoiceInputDto invoiceInputDto);

    InvoiceDto editInvoice(UUID noToEdit, InvoiceInputDto invoiceInputDto);

    UUID deleteInvoice(UUID noToDelete);

}
