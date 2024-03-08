package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
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

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepos;

    private final CatRepository catRepos;

    private final OrderRepository orderRepos;

    public CustomerServiceImplementation(CustomerRepository customerRepos, CatRepository catRepos, OrderRepository orderRepos) {
        this.customerRepos = customerRepos;
        this.catRepos = catRepos;
        this.orderRepos = orderRepos;
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
    public CustomerDto getCustomer(long idToFind) {
        Optional<Customer> customerOptional = customerRepos.findById(idToFind);
        if (customerOptional.isPresent()) {
            return CustomerMapper.transferToDto(customerOptional.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
        }
    }

    @Override
    public CustomerDto createCustomer(CustomerInputDto customerInputDto) {
        Customer newCustomer = new Customer(customerInputDto.numberOfCats(), customerInputDto.cat(), customerInputDto.order(), customerInputDto.catsitter());
        newCustomer.setNumberOfCats(customerInputDto.numberOfCats());
        newCustomer.setCat(customerInputDto.cat());
        newCustomer.setOrder(customerInputDto.order());
        newCustomer.setCatsitter(customerInputDto.catsitter());
        customerRepos.save(newCustomer);
        return CustomerMapper.transferToDto(newCustomer);
    }

    @Override
    public CustomerDto editCustomer(long idToEdit, CustomerInputDto customerInputDto) {
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
    public long deleteCustomer(long idToDelete) {
        Optional<Customer> optionalCustomer = customerRepos.findById(idToDelete);
        if (optionalCustomer.isPresent()) {
            customerRepos.deleteById((idToDelete));
            return idToDelete;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
    }

    @Override
    public CustomerDto assignCatToCustomer(Long customerId, long catId) {
        Optional<Customer> optionalCustomer = customerRepos.findById(customerId);
        Optional<Cat> optionalCat = catRepos.findById(catId);

        if (optionalCustomer.isPresent() && optionalCat.isPresent()) {
            Customer customer = optionalCustomer.get();
            Cat cat = optionalCat.get();

            customer.setCat(cat);
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer or car found with this id");
        }
    }

    @Override
    public CustomerDto assignOrderToCustomer(long customerId, long orderNo) {
        Optional<Customer> optionalCustomer = customerRepos.findById(customerId);
        Optional<Order> optionalOrder = orderRepos.findById(orderNo);

        if (optionalCustomer.isPresent() && optionalOrder.isPresent()) {
            Customer customer = optionalCustomer.get();
            Order order = optionalOrder.get();

            customer.setOrder(order);
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer or car found with this id");
        }
    }
}
