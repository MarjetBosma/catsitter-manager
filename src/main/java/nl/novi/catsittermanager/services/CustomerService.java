package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
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
public class CustomerService {

    private final CustomerRepository customerRepos;

    public CustomerService(CustomerRepository customerRepos) {
        this.customerRepos = customerRepos;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepos.findAll().stream()
                .map(CustomerMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomer(String username) {
        return customerRepos.findById(username)
                .map(CustomerMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id."));
    }

    public CustomerDto createCustomer(final CustomerInputDto customerInputDto) {
        Customer customer = CustomerMapper.transferFromDto(customerInputDto);
        customer.setEnabled(true);
        customer.setCats(new ArrayList<Cat>());
        customer.setOrders(new ArrayList<Order>());
        customerRepos.save(customer);
        return CustomerMapper.transferToDto(customer);
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
            if (customerInputDto.orders() != null) {
                customer.setOrders(customerInputDto.orders());
            }
//            if (customerInputDto.catsitters() != null) {
//                customer.setCatsitters(customerInputDto.catsitters());
//            }
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this username.");
        }
    }

    public String deleteCustomer(String username) {
        customerRepos.deleteById(username);
        return username;
    }

//    @Override
//    public CustomerDto assignCatToCustomer(Long customerId, long catId) {
//        Optional<Customer> optionalCustomer = customerRepos.findById(customerId);
//        Optional<Cat> optionalCat = catRepos.findById(catId);
//
//        if (optionalCustomer.isPresent() && optionalCat.isPresent()) {
//            Customer customer = optionalCustomer.get();
//            Cat cat = optionalCat.get();
//
//            customer.setCat(cat);
//            customerRepos.save(customer);
//            return CustomerMapper.transferToDto(customer);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer or car found with this id");
//        }
//    }

//    @Override
//    public CustomerDto assignOrderToCustomer(long customerId, long orderNo) {
//        Optional<Customer> optionalCustomer = customerRepos.findById(customerId);
//        Optional<Order> optionalOrder = orderRepos.findById(orderNo);
//
//        if (optionalCustomer.isPresent() && optionalOrder.isPresent()) {
//            Customer customer = optionalCustomer.get();
//            Order order = optionalOrder.get();
//
//            customer.setOrder(order);
//            customerRepos.save(customer);
//            return CustomerMapper.transferToDto(customer);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer or car found with this id");
//        }
//    }
}
