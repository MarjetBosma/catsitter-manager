package nl.novi.catsittermanager.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(final String username) {
        return customerRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username."));
    }

    public Customer createCustomer(final Customer customer) {
        customer.setEnabled(true);
        customer.setRole(Role.CUSTOMER);
        customer.setOrders(new ArrayList<Order>());
        customer.setCats(new ArrayList<Cat>());
        return customerRepository.save(customer);
    }

    // todo: uitzoeken waarom deze een 500 error geeft, mogelijk iets met de orders of cats?
    public Customer editCustomer(String username, Customer customer) {
        if (customerRepository.findById(username).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username.");
        }
        return customerRepository.save(customer);
    }

    public String deleteCustomer(String username) {
        if (!customerRepository.existsById(username)) {
            throw new RecordNotFoundException("No customer found with this username.");
        }
        customerRepository.deleteById(username);
        return username;
    }

}
