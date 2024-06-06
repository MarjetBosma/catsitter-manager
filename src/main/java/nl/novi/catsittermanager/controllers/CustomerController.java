package nl.novi.catsittermanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(HttpServletRequest request) {
        List<CustomerResponse> customerResponseList = customerService.getAllCustomers().stream()
                .map(CustomerMapper::CustomerToCustomerResponse)
                .toList();
        return ResponseEntity.ok(customerResponseList);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") final String username) {
        Customer customer = customerService.getCustomer(username);
        return ResponseEntity.ok(CustomerMapper.CustomerToCustomerResponse(customer));
    }

    @GetMapping("/customer/{id}/cats")
    public ResponseEntity<List<CatResponse>> getAllCatsByCustomer(@PathVariable("id") final String username) {
        List<Cat> cats = customerService.getAllCatsByCustomer(username);
        List<CatResponse> catResponseList = cats.stream()
                .map(CatMapper::CatToCatResponse)
                .toList();
        return ResponseEntity.ok(catResponseList);
    }

    @GetMapping("/customer/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByCustomer(@PathVariable("id") final String username) {
        List<Order> orders = customerService.getAllOrdersByCustomer(username);
        List<OrderResponse> orderResponseList = orders.stream()
                .map(OrderMapper::OrderToOrderResponse)
                .toList();
        return ResponseEntity.ok(orderResponseList);
    }

    @GetMapping("/order/{id}/catsitters")
    public ResponseEntity<List<CatsitterResponse>> getAllCatsittersByCustomer(@PathVariable("id") final String username) {
        List<Catsitter> catsitters = customerService.getAllCatsittersByCustomer(username);
        List<CatsitterResponse> customerResponseList = catsitters.stream()
                .map(CatsitterMapper::CatsitterToCatsitterResponse)
                .toList();
        return ResponseEntity.ok(customerResponseList);
    }

   @PostMapping("/customer")
   public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody final CustomerRequest customerRequest) throws URISyntaxException {
       Customer customer = customerService.createCustomer(CustomerMapper.CustomerRequestToCustomer(customerRequest));
       return ResponseEntity.created(new URI("/customer/" + customer.getUsername())).body(CustomerMapper.CustomerToCustomerResponse(customer));
   }

    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerResponse> editCustomer(@PathVariable("id") final String username, @RequestBody final CustomerRequest customerRequest) {
        Customer customer = customerService.editCustomer(username, CustomerMapper.CustomerRequestToCustomer(customerRequest));
        return ResponseEntity.ok().body(CustomerMapper.CustomerToCustomerResponse(customer));
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") final String username) {
        customerService.deleteCustomer(username);
        return ResponseEntity.ok().body("Customer " + username + " removed from database");
    }
}
