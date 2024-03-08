package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomer(long idToFind);

    CustomerDto createCustomer(CustomerInputDto customerInputDto);

    CustomerDto editCustomer(long idToEdit, CustomerInputDto customerInputDto);

    long deleteCustomer (long idToDelete);

    CustomerDto assignCatToCustomer(Long customerId, long catId);

    CustomerDto assignOrderToCustomer(long customerId, long orderNo);
}
