package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.services.CatsitterService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CatsitterController {

    private final CatsitterService catsitterService;

    @GetMapping("/catsitters")
    public ResponseEntity<List<CatsitterResponse>> getAllCatsitters() {
        List<CatsitterResponse> catsitterResponseList = catsitterService.getAllCatsitters().stream()
                .map(CatsitterMapper::CatsitterToCatsitterResponse)
                .toList();
        return ResponseEntity.ok(catsitterResponseList);
    }

    @GetMapping("/catsitter/{id}")
    public ResponseEntity<CatsitterResponse> getCatSitter(@PathVariable("id") final String username) {
        Catsitter catsitter = catsitterService.getCatsitter(username);
        return ResponseEntity.ok(CatsitterMapper.CatsitterToCatsitterResponse(catsitter));
    }

    @GetMapping("/catsitter/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByCatsitter(@PathVariable("id") final String username) {
        List<Order> orders = catsitterService.getAllOrdersByCatsitter(username);
        List<OrderResponse> orderResponseList = orders.stream()
                .map(OrderMapper::OrderToOrderResponse)
                .toList();
        return ResponseEntity.ok(orderResponseList);
    }

    @GetMapping("/catsitter/{id}/orders/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomersByCatsitter(@PathVariable("id") final String username) {
        List<Customer> customers = catsitterService.getAllCustomersByCatsitter(username);
        List<CustomerResponse> customerResponseList = customers.stream()
                .map(CustomerMapper::CustomerToCustomerResponse)
                .toList();
        return ResponseEntity.ok(customerResponseList);
    }

   @PostMapping("/catsitter")
   public ResponseEntity<CatsitterResponse> createCatsitter(@Valid @RequestBody final CatsitterRequest catsitterRequest) throws URISyntaxException {
       Catsitter catsitter = catsitterService.createCatsitter(CatsitterMapper.CatsitterRequestToCatsitter(catsitterRequest));
       return ResponseEntity.created(new URI("/catsitter/" + catsitter.getUsername())).body(CatsitterMapper.CatsitterToCatsitterResponse(catsitter));
   }

   @PutMapping("/catsitter/{id}")
   public ResponseEntity<CatsitterResponse> editCatsitter(@PathVariable("id") final String username, @RequestBody final CatsitterRequest catsitterRequest) {
       Catsitter catsitter = catsitterService.editCatsitter(username, CatsitterMapper.CatsitterRequestToCatsitter(catsitterRequest));
       return ResponseEntity.ok().body(CatsitterMapper.CatsitterToCatsitterResponse(catsitter));
   }

   @DeleteMapping("/catsitter/{id}")
   public ResponseEntity<Object> deleteCatsitter(@PathVariable("id") final String username) {
       catsitterService.deleteCatsitter(username);
       return ResponseEntity.ok().body("Catsitter with username " + username + " removed from database.");
   }
}
