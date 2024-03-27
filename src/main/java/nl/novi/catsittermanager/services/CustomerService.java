package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepos;

    public List<CustomerDto> getAllCustomers() {
        return customerRepos.findAll()
                .stream()
                .map(CustomerMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomer(String username) {
        return customerRepos.findById(username)
                .map(CustomerMapper::transferToDto)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username."));
    }

    public CustomerDto createCustomer(final CustomerInputDto customerInputDto) {
        Customer newCustomer = CustomerMapper.transferFromInputDto(customerInputDto);
        newCustomer.setEnabled(true);
        newCustomer.setRole(Role.CUSTOMER);
        newCustomer.setOrders(new ArrayList<Order>());
        newCustomer.setCats(new ArrayList<Cat>());
        customerRepos.save(newCustomer);
        return CustomerMapper.transferToDto(newCustomer);
    }

    public CustomerDto editCustomer(String username, CustomerInputDto customerInputDto) {
        Optional<Customer> optionalCustomer = customerRepos.findById(username);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customerInputDto.username() != null) {
                customer.setUsername(customerInputDto.username());
            }
            if (customerInputDto.password() != null) {
                customer.setPassword(customerInputDto.password());
            }
            if (customerInputDto.name() != null) {
                customer.setName(customerInputDto.name());
            }
            if (customerInputDto.address() != null) {
                customer.setAddress(customerInputDto.address());
            }
            if (customerInputDto.email() != null) {
                customer.setEmail(customerInputDto.email());
            }
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username.");
        }
    }

    public String deleteCustomer(String username) {
        customerRepos.deleteById(username);
        return username;
    }

}
