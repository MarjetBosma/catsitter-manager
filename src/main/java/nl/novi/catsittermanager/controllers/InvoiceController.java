//package nl.novi.catsittermanager.controllers;
//
//import jakarta.validation.Valid;
//import nl.novi.catsittermanager.dtos.IdInputDto;
//import nl.novi.catsittermanager.dtos.customer.CustomerDto;
//import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
//import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
//import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
//import nl.novi.catsittermanager.exceptions.ValidationException;
//import nl.novi.catsittermanager.services.InvoiceServiceImplementation;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
//import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;
//
//@RestController
//@RequestMapping("/invoices")
//
//public class InvoiceController {
//
//    private final InvoiceServiceImplementation invoiceService;
//
//    public InvoiceController(InvoiceServiceImplementation invoiceService) {
//        this.invoiceService = invoiceService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
//        return ResponseEntity.ok(invoiceService.getAllInvoices());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable Long id) {
//        if (id > 0) {
//            InvoiceDto invoiceDto = InvoiceServiceImplementation.getInvoiceId();
//            return ResponseEntity.ok(invoiceDto);
//        } else {
//            throw new RecordNotFoundException("No invoice found with this id");
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<CustomerDto> addInvoice(@RequestBody InvoiceInputDto invoiceInputDto, BindingResult br) {
//        if (br.hasFieldErrors()) {
//            throw new ValidationException(checkForBindingResult(br));
//        } else {
//            InvoiceDto savedInvoice;
//            savedInvoice = invoiceService.createInvoice(invoiceInputDto);
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder
//                            .fromCurrentRequest()
//                            .path("/" + savedInvoice.id).toUriString());
//            return ResponseEntity.created(uri).body(savedInvoice);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<InvoiceDto> updateCustomer(@PathVariable long id, @RequestBody InvoiceInputDto invoice) {
//        InvoiceDto changeInvoiceId = invoiceService.updateInvoice(id, invoice);
//
//        return ResponseEntity.ok().body(changeInvoiceId);
//    }
//
//    @PutMapping("/{id}/order")
//    public ResponseEntity<Object> assignOrderToInvoice(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        invoiceService.assignOrderToInvoice(id, input.id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteInvoice(@PathVariable("id") Long id) {
//        invoiceService.deleteInvoice(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//}
