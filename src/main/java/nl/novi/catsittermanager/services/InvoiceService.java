package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepos;

    public InvoiceService(InvoiceRepository invoiceRepos) {
        this.invoiceRepos = invoiceRepos;
    }

    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepos.findAll().stream()
                .map(InvoiceMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto getInvoice(UUID idToFind) {
        return invoiceRepos.findById(idToFind)
                .map(InvoiceMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    public InvoiceDto createInvoice(InvoiceInputDto invoiceInputDto) {
        Invoice newInvoice = InvoiceMapper.transferFromDto((invoiceInputDto));
        newInvoice.setOrder(invoiceInputDto.order());
        invoiceRepos.save(newInvoice);
        return InvoiceMapper.transferToDto(newInvoice);
    }

    public InvoiceDto editInvoice(UUID idToEdit, InvoiceInputDto invoiceInputDto) {
        Optional<Invoice> optionalInvoice = invoiceRepos.findById(idToEdit);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            if (invoiceInputDto.invoiceDate() != null) {
                invoice.setInvoiceNo(invoiceInputDto.invoiceNo());
            }
            if (invoice.getInvoiceDate() != null) {
                invoice.setAmount(invoiceInputDto.amount());
            }
            if (invoice.getPaid() != null) {
                invoice.setPaid(invoiceInputDto.paid());
            }
            if (invoice.getOrder() != null) {
                invoice.setOrder(invoiceInputDto.order());
            }
            invoiceRepos.save(invoice);
            return InvoiceMapper.transferToDto(invoice);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
        }
    }

    public UUID deleteInvoice(UUID idToDelete) {
        invoiceRepos.deleteById(idToDelete);
        return idToDelete;
    }


}
