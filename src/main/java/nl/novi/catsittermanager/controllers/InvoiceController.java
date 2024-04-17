package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class InvoiceController {

    private final InvoiceService invoiceService;

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
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody final InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceService.createInvoice(InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), invoiceRequest.orderNo());
        return ResponseEntity.status(HttpStatus.CREATED).body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
    }
    // todo: beslissen of ik een versie met validation exception wil gebruiken
    // todo: afhandelen foutmelding als het ingegeven ordernummer al een invoice heeft

    @PutMapping("/invoice/{id}")
    public ResponseEntity<InvoiceResponse> editInvoice(@PathVariable("id") final UUID idToEdit, @RequestBody final InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceService.editInvoice(idToEdit, InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), invoiceRequest.orderNo());
        return ResponseEntity.ok().body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
    }

    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<Object> deleteInvoice(@PathVariable("id") final UUID idToDelete) {
        invoiceService.deleteInvoice(idToDelete);
        return ResponseEntity.ok().body("Invoice with id " + idToDelete + " removed from database");
    }
}
