package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Invoice createInvoice(final Invoice invoice, final UUID orderNo) {
        Order order = orderService.getOrder(orderNo);
        invoice.setOrder(order);
        return invoiceRepository.save(invoice);
    }
    public boolean existsByOrderNo(UUID orderNo) {
        return invoiceRepository.existsByOrder_OrderNo(orderNo);
    }

    public Invoice editInvoice(final UUID idToEdit, final Invoice updatedInvoice, final UUID orderNo) {
        Invoice existingInvoice = invoiceRepository.findById(idToEdit)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found with this id."));

        updatedInvoice.setOrder(existingInvoice.getOrder());
        existingInvoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
        existingInvoice.setAmount(updatedInvoice.getAmount());
        existingInvoice.setPaid(updatedInvoice.getPaid());

        return invoiceRepository.save(existingInvoice);
    }

    public UUID deleteInvoice(UUID idToDelete) {
        if (!invoiceRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No invoice found with this id.");
        }
        invoiceRepository.deleteById(idToDelete);
        return idToDelete;
    }
}
