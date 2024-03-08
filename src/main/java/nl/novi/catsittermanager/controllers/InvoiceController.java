package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.InvoiceServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/invoice")

public class InvoiceController {

    private final InvoiceServiceImplementation invoiceService;

    public InvoiceController(InvoiceServiceImplementation invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable("id") UUID idToFind) {
//        if (idToFind > 0) {
        InvoiceDto invoiceDto = invoiceService.getInvoice(idToFind);
        return ResponseEntity.ok(invoiceDto);
//        } else {
//            throw new RecordNotFoundException("No invoice found with this number");
//        }
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceInputDto invoiceInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            InvoiceDto savedInvoice = invoiceService.createInvoice(invoiceInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedInvoice).toUriString());
            return ResponseEntity.created(uri).body(savedInvoice);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> editCustomer(@PathVariable("id") UUID idToEdit, @RequestBody InvoiceInputDto invoice) {
        InvoiceDto editedInvoice = invoiceService.editInvoice(idToEdit, invoice);

        return ResponseEntity.ok().body(editedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteInvoice(@PathVariable("id") UUID idToDelete) {
        invoiceService.deleteInvoice(idToDelete);
        return ResponseEntity.ok().body("Invoice with id " + idToDelete + " removed from database");
    }

//    @PutMapping("/{id}/order")
//    public ResponseEntity<Object> assignOrderToInvoice(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        invoiceService.assignOrderToInvoice(id, input.id);
//        return ResponseEntity.noContent().build();
//    }
}
