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

    @GetMapping("/{username}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("username") final String username) {
            CustomerDto customerDto = customerService.getCustomer(username);
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

    @PutMapping("/{username}")
    public ResponseEntity<CustomerDto> editCustomer(@PathVariable("username") final String username, @RequestBody final CustomerInputDto customer) {
        CustomerDto editedCustomer = customerService.editCustomer(username, customer);
        return ResponseEntity.ok().body(editedCustomer);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("username") final String username) {
        customerService.deleteCustomer(username);
        return ResponseEntity.ok().body("Customer " + username +  " removed from database");
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
