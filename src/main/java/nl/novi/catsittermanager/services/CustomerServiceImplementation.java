package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;

import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepos;


    public CustomerServiceImplementation(CustomerRepository customerRepos) {
        this.customerRepos = customerRepos;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepos.findAll().stream()
                .map(CustomerMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomer(UUID idToFind) {
        return customerRepos.findById(idToFind)
                .map(CustomerMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    @Override
    public CustomerDto createCustomer(CustomerInputDto customerInputDto) {
        Customer customer = Customer.builder()
                .numberOfCats(customerInputDto.numberOfCats())
                .cat(customerInputDto.cat())
                .order(customerInputDto.order())
                .catsitter(customerInputDto.catsitter())
                .build();
        customerRepos.save(customer);
        return CustomerMapper.transferToDto(customer);
    }

    @Override
    public CustomerDto editCustomer(UUID idToEdit, CustomerInputDto customerInputDto) {
        Optional<Customer> optionalCustomer = customerRepos.findById(idToEdit);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customerInputDto.numberOfCats() != 0) {
                customer.setNumberOfCats(customerInputDto.numberOfCats());
            }
            if (customerInputDto.order() != null) {
                customer.setOrder(customerInputDto.order());
            }
            if (customerInputDto.cat() != null) {
                customer.setCat(customerInputDto.cat());
            }
            if (customerInputDto.catsitter() != null) {
                customer.setCatsitter(customerInputDto.catsitter());
            }
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
        }
    }

    @Override
    public UUID deleteCustomer(UUID idToDelete) {
        customerRepos.deleteById(idToDelete);
        return idToDelete;
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
