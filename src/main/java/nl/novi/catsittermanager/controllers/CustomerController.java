package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CustomerServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/customer")

public class CustomerController {

    private final CustomerServiceImplementation customerService;

    public CustomerController(CustomerServiceImplementation customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") final UUID idToFind) {
            CustomerDto customerDto = customerService.getCustomer(idToFind);
            return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody final CustomerInputDto customerInputDto, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CustomerDto savedCustomer;
            savedCustomer = customerService.createCustomer(customerInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCustomer).toUriString());
            return ResponseEntity.created(uri).body(savedCustomer);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> editCustomer(@PathVariable("id") final UUID idToEdit, @RequestBody final CustomerInputDto customer) {
        CustomerDto editedCustomer = customerService.editCustomer(idToEdit, customer);
        return ResponseEntity.ok().body(editedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") final UUID idToDelete) {
        customerService.deleteCustomer(idToDelete);
        return ResponseEntity.ok().body("Customer with id " + idToDelete +  " removed from database");
    }

//    @PutMapping("/{customerId}/{catId}")
//    public CustomerDto assignCatToCustomer(@PathVariable("customerId") Long customerId, @PathVariable("catId") long catId) {
//        return customerService.assignCatToCustomer(customerId, catId);
//    }
//
//    @PutMapping("/{customerId}/{orderId}")
//    public CustomerDto assignOrderToCustomer(@PathVariable("customerId") Long customerId, @PathVariable("orderId") long orderId) {
//        return customerService.assignOrderToCustomer(customerId, orderId);
//    }

}
