package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderService orderService;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoice(final UUID idToFind) {
        return invoiceRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found with this id."));
    }
    // todo: deze geeft een authentication error / null pointer exception, waarom?

    public Invoice createInvoice(final Invoice invoice, final UUID orderNo) {
            Order order = orderService.getOrder(orderNo);
            invoice.setOrder(order);
            return invoiceRepository.save(invoice);
        }
    // todo: deze geeft een authentication error / null pointer exception, waarom?
    // beslissen of ik field validation wil gebruiken


    public Invoice editInvoice(final UUID idToEdit, final Invoice invoice, UUID orderNo) {
        if (invoiceRepository.findById(idToEdit).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found with this id.");
        }
        Order order = orderService.getOrder(orderNo);
        invoice.setOrder(order);
        return invoiceRepository.save(invoice);
    }

    public UUID deleteInvoice(UUID idToDelete) {
        if (!invoiceRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No invoice found with this id.");
        }
        invoiceRepository.deleteById(idToDelete);
        return idToDelete;
    }
}
