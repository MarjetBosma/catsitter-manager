package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.invoice.InvoiceRequest;
import nl.novi.catsittermanager.dtos.invoice.InvoiceResponse;
import nl.novi.catsittermanager.exceptions.InvoiceAlreadyExistsForThisOrderException;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.InvoiceMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.services.InvoiceService;
import nl.novi.catsittermanager.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        try {
            Order order = orderService.getOrder(orderNo);
            Invoice invoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest, order);
            invoice.setPaid(false);

            Invoice savedInvoice = invoiceService.createInvoice(invoice, orderNo);
            return ResponseEntity.created(new URI("/invoice/" + savedInvoice.getInvoiceNo()))
                    .body(InvoiceMapper.InvoiceToInvoiceResponse(savedInvoice));
        } catch (InvoiceAlreadyExistsForThisOrderException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("An invoice already exists for order " + orderNo + ".");
        } catch (RecordNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error occurred while creating the invoice.");
        }
    }

//    @PostMapping("/invoice")
//    public ResponseEntity<?> createInvoice(@Valid @RequestBody final InvoiceRequest invoiceRequest) throws URISyntaxException {
//        UUID orderNo = invoiceRequest.orderNo();
//        try {
//            // Call the service method with the request and orderNo
//            Invoice invoice = invoiceService.createInvoice(invoiceRequest, orderNo);
//            System.out.println("Invoice created successfully for orderNo: " + orderNo);
//            return ResponseEntity.created(new URI("/invoice/" + invoice.getInvoiceNo())).body(InvoiceMapper.InvoiceToInvoiceResponse(invoice));
//        } catch (InvoiceAlreadyExistsForThisOrderException exception) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("An invoice already exists for order " + orderNo + ".");
//        } catch (RecordNotFoundException exception) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Order not found");
//        }
//    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<InvoiceResponse> editInvoice(@PathVariable("id") final UUID idToEdit, @Valid @RequestBody final InvoiceRequest invoiceRequest) {
        UUID orderNo = invoiceRequest.orderNo();
        Order order = orderService.getOrder(orderNo);

        if (order == null) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id");
        }

        Invoice updatedInvoice = InvoiceMapper.InvoiceRequestToInvoice(invoiceRequest, order);
        updatedInvoice.setInvoiceNo(idToEdit);
        Invoice savedInvoice = invoiceService.editInvoice(updatedInvoice);

        return ResponseEntity.ok().body(InvoiceMapper.InvoiceToInvoiceResponse(savedInvoice));
    }


    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<Object> deleteInvoice(@PathVariable("id") final UUID idToDelete) {
        invoiceService.deleteInvoice(idToDelete);
        return ResponseEntity.ok().body("Invoice with id " + idToDelete + " removed from database.");
    }
}
