package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.services.CustomerService;
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

@RestController
@RequestMapping("/customer")

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customerResponseList = customerService.getAllCustomers().stream()
                .map(CustomerMapper::CustomerToCustomerResponse)
                .toList();
        return ResponseEntity.ok(customerResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") final String username) {
        Customer customer = customerService.getCustomer(username);
        return ResponseEntity.ok(CustomerMapper.CustomerToCustomerResponse(customer));
    }
    // todo: is het mogelijk/wenselijk om een aparte URI, GET request en methode aan te maken voor de orders van een bepaalde catsitter? Bijv. /customer/order of /customer/cat

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody final CustomerRequest customerRequest) {
        Customer customer = customerService.createCustomer(CustomerMapper.CustomerRequestToCustomer(customerRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.CustomerToCustomerResponse(customer));
    }
    // todo: uitzoeken waarom de extra parameter username in de service hier een probleem geeft, en of ik die username validatie wellicht ergens anders moet doen

    // todo: beslissen of ik onderstaande versie met optie voor validation exception wil implementeren
//    @PostMapping
//    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody final CustomerRequest customerRequest, final BindingResult br) {
//        if (br.hasFieldErrors()) {
//            throw new ValidationException(checkForBindingResult(br));
//        } else {
//            Customer customer = customerService.createCustomer(CustomerMapper.CustomerRequestToCustomer(customerRequest));
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder
//                            .fromCurrentRequest()
//                            .path("/" + customer).toUriString());
//            return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.CustomerToCustomerResponse(customer));
//        }
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> editCustomer(@PathVariable("id") final String username, @RequestBody final CustomerRequest customerRequest) {
        Customer customer = customerService.editCustomer(username, CustomerMapper.CustomerRequestToCustomer(customerRequest));
        return ResponseEntity.ok().body(CustomerMapper.CustomerToCustomerResponse(customer));
    }
    // todo: deze geeft een authentication error, waarom?

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") final String username) {
        customerService.deleteCustomer(username);
        return ResponseEntity.ok().body("Customer " + username + " removed from database");
    }

}
