package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomer(String username);

    CustomerDto createCustomer(CustomerInputDto customerInputDto);

    CustomerDto editCustomer(String username, CustomerInputDto customerInputDto);

    String deleteCustomer (String username);

}
