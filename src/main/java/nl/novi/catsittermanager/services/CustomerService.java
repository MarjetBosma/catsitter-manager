package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomer(UUID idToFind);

    CustomerDto createCustomer(CustomerInputDto customerInputDto);

    CustomerDto editCustomer(UUID idToEdit, CustomerInputDto customerInputDto);

    UUID deleteCustomer(UUID idToDelete);
}
