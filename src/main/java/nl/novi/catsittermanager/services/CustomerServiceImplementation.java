package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.mappers.CustomerMapper;
import nl.novi.catsittermanager.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImplementation implements CustomerService {

//    private final CustomerRepository catSitterRepos;
//
//    private final CatSitterServiceImplementation customerService;
//
//    private final OrderServiceImplementation orderService;

    private List<Customer> customers = new ArrayList<>(); // voor testen zonder database

//    public CustomerServiceImplementation(CatsitterServiceImplementation catSitterService, OrderServiceImplementation orderService) {
//        this.customerRepos = customerRepos;
//        this.catSitterService = catSitterService;
//        this.orderService = orderService;
//    }

    public CustomerServiceImplementation() { // Alleen voor testen zonder database
        customers.add(new Customer(1L,2, "Lijst van kattem op naam", "Lijst met orders", "Lijst met kattenoppassen"));
        customers.add(new Customer(2L,1, "Lijst van katten op naam", "Lijst met orders", "Lijst met kattenoppassen"));
    }
    @Override
    public List<CustomerDto> getAllCustomers() {
        //        List<Customer> customerList = customerRepos.findAll(); // Deze is voor als de database gevuld is
        List<CustomerDto> customerDtoList = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDto customerDto = CustomerMapper.transferToDto(customer);
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    @Override
    public CustomerDto getCustomer(long idToFind) {
        for (Customer customer : customers) {
            if (customer.getId() == idToFind) {
                return CustomerMapper.transferToDto(customer);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
    }

    @Override
    public CustomerDto createCustomer(CustomerInputDto customerInputDto) {
        Customer newCustomer = CustomerMapper.transferFromDto(customerInputDto);
        customers.add(newCustomer);
        return CustomerMapper.transferToDto(newCustomer);
    }

    @Override
    public CustomerDto editCustomer(long idToEdit, CustomerInputDto customerInputDto) {
        for (Customer customer : customers) {
            if (customer.getId() == idToEdit) {
                if (customerInputDto.numberOfCats() != 0) {
                    customer.setNumberOfCats(customerInputDto.numberOfCats());
                }
                if (customerInputDto.orderList() != null) {
                    customer.setOrderList(customerInputDto.orderList());
                }
                if (customerInputDto.catListByName() != null) {
                    customer.setCatListByName(customerInputDto.catListByName());
                }
                if (customerInputDto.catSitterList() != null) {
                    customer.setCatSitterList(customerInputDto.catSitterList());
                }
                return CustomerMapper.transferToDto(customer);
            }

        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found with this id.");
    }

    @Override
    public void deleteCustomer(long idToDelete) {
        for (Customer customer : customers) {
            if (customer.getId() == idToDelete) {
                customers.remove(customer);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
    }

    // Toevoegen: assignCatToCustomer, assignCatSitterToCustomer, assignOrderToCustomer
}
