package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImplementation implements InvoiceService {

//    private final InvoiceRepository invoiceRepos;

//    private final OrderRepository orderRepos;

//    private final OrderServiceImplementation orderService;

    private List<Invoice> invoices = new ArrayList<>(); // voor testen zonder database

//    public InvoiceServiceImplementation(InvoiceRepository invoiceRepos, OrderServiceImplementation orderService) {
//        this.invoiceRepos = invoiceRepos;
//        this.orderService = orderService;
//    }

    public InvoiceServiceImplementation() { // Bedoeld voor testen zonder database
        invoices.add(new Invoice(1L, LocalDate.parse("2023-09-15"), 300.00, true, "Hoort bij order 1"));
        invoices.add(new Invoice(2L, LocalDate.parse("2024-02-08"), 200.00, false, "Hoort bij order 2"));
    }

    @Override
    public List<InvoiceDto> getAllInvoices() {
//        List<Invoice> invoiceList = invoiceRepos.findAll(); // Deze is voor als de database gevuld is
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            InvoiceDto invoiceDto = InvoiceMapper.transferToDto(invoice);
            invoiceDtoList.add(invoiceDto);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto getInvoice(long idToFind) { // Nu alleen nog voor gebruik zonder database
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNo() == idToFind) {
                return InvoiceMapper.transferToDto(invoice);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
    }

    @Override
    public InvoiceDto createInvoice(InvoiceInputDto invoiceInputDto) {
        Invoice newInvoice = InvoiceMapper.transferFromDto(invoiceInputDto);
        invoices.add(newInvoice);
        return InvoiceMapper.transferToDto(newInvoice);
    }

    @Override
    public InvoiceDto editInvoice(long idToEdit, InvoiceInputDto invoiceInputDto) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNo() == idToEdit) {
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
                return InvoiceMapper.transferToDto(invoice);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
    }

    @Override
    public long deleteInvoice(long idToDelete) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNo() == idToDelete) {
                invoices.remove(invoice);
                return idToDelete;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
    }
}