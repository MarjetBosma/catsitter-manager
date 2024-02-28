package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CustomerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepos;
//
//    private final CatSitterServiceImplementation customerService;
//
//    private final OrderServiceImplementation orderService;

    public CustomerServiceImplementation(CustomerRepository customerRepos
//    , CatsitterServiceImplementation catSitterService, OrderServiceImplementation orderService
    ) {
        this.customerRepos = customerRepos;
//        this.catSitterService = catSitterService;
//        this.orderService = orderService;
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
        Customer newCustomer = new Customer();
        newCustomer.setNumberOfCats(customerInputDto.numberOfCats());
        newCustomer.setCatListByName(customerInputDto.catListByName());
        newCustomer.setOrderList(customerInputDto.orderList());
        newCustomer.setCatsitterList(customerInputDto.catsitterList());
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
            if (customerInputDto.orderList() != null) {
                customer.setOrderList(customerInputDto.orderList());
            }
            if (customerInputDto.catListByName() != null) {
                customer.setCatListByName(customerInputDto.catListByName());
            }
            if (customerInputDto.catsitterList() != null) {
                customer.setCatsitterList(customerInputDto.catsitterList());
            }
            customerRepos.save(customer);
            return CustomerMapper.transferToDto(customer);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
        }
    }

    @Override
    public String deleteCustomer(long idToDelete) {
        Optional<Customer> optionalCustomer = customerRepos.findById(idToDelete);
        if (optionalCustomer.isPresent()) {
            customerRepos.deleteById((idToDelete));
            return "Customer with id " + idToDelete + " removed from database";
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
    }
}
// Toevoegen: assignCatToCustomer, assignCatSitterToCustomer, assignOrderToCustomer