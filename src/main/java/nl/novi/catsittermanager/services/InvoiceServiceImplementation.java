package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceServiceImplementation implements InvoiceService {

    private final InvoiceRepository invoiceRepos;

    public InvoiceServiceImplementation(InvoiceRepository invoiceRepos) {
        this.invoiceRepos = invoiceRepos;
    }

    @Override
    public List<InvoiceDto> getAllInvoices() {
        List<Invoice> invoiceList = invoiceRepos.findAll();
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();

        for (Invoice invoice : invoiceList) {
            InvoiceDto invoiceDto = InvoiceMapper.transferToDto(invoice);
            invoiceDtoList.add(invoiceDto);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto getInvoice(UUID idToFind) {
        Optional<Invoice> invoiceOptional = invoiceRepos.findById(idToFind);
        if (invoiceOptional.isPresent()) {
            return InvoiceMapper.transferToDto(invoiceOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
        }
    }

    @Override
    public InvoiceDto createInvoice(InvoiceInputDto invoiceInputDto) {
//        Invoice newInvoice = new Invoice(invoiceInputDto.invoiceNo(), invoiceInputDto.invoiceDate(), invoiceInputDto.amount(), invoiceInputDto.paid(), invoiceInputDto.order());
//        newInvoice.setInvoiceNo(invoiceInputDto.invoiceNo());
//        newInvoice.setInvoiceDate(invoiceInputDto.invoiceDate());
//        newInvoice.setAmount(invoiceInputDto.amount());
//        newInvoice.setPaid(invoiceInputDto.paid());
//        newInvoice.setOrder(invoiceInputDto.order());
//        invoiceRepos.save(newInvoice);
//        return InvoiceMapper.transferToDto(newInvoice);
        return null;
    }

    @Override
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

    @Override
    public UUID deleteInvoice(UUID idToDelete) {
        Optional<Invoice> optionalInvoice = invoiceRepos.findById(idToDelete);
        if (optionalInvoice.isPresent()) {
            invoiceRepos.deleteById(idToDelete);
            return idToDelete;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
        }
    }
}