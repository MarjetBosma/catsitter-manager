package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.services.InvoiceService;
import nl.novi.catsittermanager.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final OrderService orderService;

    private final AuthenticationManager authentication;

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoiceResponseList = invoiceService.getAllInvoices().stream()
                .map(InvoiceMapper::InvoiceToInvoiceResponse)
                .toList();
        return ResponseEntity.ok(invoiceResponseList);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable("id") final UUID idToFind) {
        Invoice invoice = invoiceService.getInvoice(idToFind);
        return ResponseEntity.ok(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
    }

    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody final InvoiceRequest invoiceRequest) throws URISyntaxException {
        UUID orderNo = invoiceRequest.orderNo();
        System.out.println("Inside createInvoice - orderNo: " + orderNo);
        try {
            Invoice invoice = invoiceService.createInvoice(InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), orderNo);
            System.out.println("Invoice created successfully for orderNo: " + orderNo);
            return ResponseEntity.created(new URI("/invoice/" + invoice.getInvoiceNo())).body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
        } catch (InvoiceAlreadyExistsForThisOrderException exception) {
            System.out.println("Invoice already exists for orderNo: " + orderNo);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("An invoice already exists for order number: " + orderNo);
        } catch (RecordNotFoundException exception) {
            System.out.println("Order not found for orderNo: " + orderNo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }

    // todo: geeft 403 forbidden in Postman, tests slagen wel
    @PutMapping("/invoice/{id}")
    public ResponseEntity<InvoiceResponse> editInvoice(@PathVariable("id") final UUID idToEdit, @Valid @RequestBody final InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceService.editInvoice(idToEdit, InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), invoiceRequest.orderNo());
        return ResponseEntity.ok().body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
    }

    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<Object> deleteInvoice(@PathVariable("id") final UUID idToDelete) {
        invoiceService.deleteInvoice(idToDelete);
        return ResponseEntity.ok().body("Invoice with id " + idToDelete + " removed from database.");
    }
}
