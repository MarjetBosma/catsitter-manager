package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepos;

    private final CatRepository catRepos;

    public CustomerServiceImplementation(CustomerRepository customerRepos, CatRepository catRepos) {
        this.customerRepos = customerRepos;
        this.catRepos = catRepos;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        List<Customer> customerList = customerRepos.findAll();

        for (Customer customer : customerList) {
            CustomerDto customerDto = CustomerMapper.transferToDto(customer);
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    @Override
    public CustomerDto getCustomer(UUID idToFind) {
        Optional<Customer> customerOptional = customerRepos.findById(idToFind);
        if (customerOptional.isPresent()) {
            return CustomerMapper.transferToDto(customerOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
        }
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
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
        }
    }

    @Override
    public UUID deleteCustomer(UUID idToDelete) {
        Optional<Customer> optionalCustomer = customerRepos.findById(idToDelete);
        if (optionalCustomer.isPresent()) {
            customerRepos.deleteById((idToDelete));
            return idToDelete;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
    }
}
// Toevoegen: assignCatToCustomer, assignCatSitterToCustomer, assignOrderToCustomer