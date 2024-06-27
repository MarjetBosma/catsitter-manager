package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
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

    public Invoice createInvoice(final InvoiceRequest invoiceRequest, final UUID orderNo) {

        Order order = orderService.getOrder(orderNo);

        if (orderService.hasExistingInvoice(orderNo)) {
            throw new InvoiceAlreadyExistsForThisOrderException(order.getOrderNo());
        }

        Invoice invoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest, order);
        invoice.setPaid(false);

        return invoiceRepository.save(invoice);
    }

    public Invoice editInvoice(final Invoice updatedInvoice) {
        Invoice existingInvoice = invoiceRepository.findById(updatedInvoice.getInvoiceNo())
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found with this id."));

        existingInvoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
        existingInvoice.setAmount(updatedInvoice.getAmount());
        existingInvoice.setPaid(updatedInvoice.getPaid());
        existingInvoice.setOrder(updatedInvoice.getOrder());

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
