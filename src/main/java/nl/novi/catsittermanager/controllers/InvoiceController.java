package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.helpers.ControllerHelper.checkForBindingResult;

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

//    @PostMapping("/invoice")
//    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody final InvoiceRequest invoiceRequest) {
//        Invoice invoice = invoiceService.createInvoice(InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), invoiceRequest.orderNo());
//        return ResponseEntity.status(HttpStatus.CREATED).body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
//    }

    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody final InvoiceRequest invoiceRequest, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException("Validation failed: " + checkForBindingResult(br));
        } else {
            UUID orderNo = invoiceRequest.orderNo();
            if (invoiceService.existsByOrderNo(orderNo)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("An invoice already exists for order number: " + orderNo);
            } else {
                Invoice invoice = invoiceService.createInvoice(InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest), orderNo);
                URI uri = URI.create(
                        ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/api/invoice")
                                .toUriString());
                return ResponseEntity.status(HttpStatus.CREATED).body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
            }
        }
    }

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
