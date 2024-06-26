package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.CustomerRequestFactory;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {

    @Test
    void testCustomerToCustomerResponse() {

        // Arrange
        Customer customer = CustomerFactory.randomCustomer().build();

        // Act
        CustomerResponse customerResponse = CustomerMapper.CustomerToCustomerResponse(customer);

        // Assert
        assertEquals(customer.getUsername(), customerResponse.username());
        assertEquals(customer.getName(), customerResponse.name());;
        assertEquals(customer.getAddress(), customerResponse.address());
        assertEquals(customer.getEmail(), customerResponse.email());
    }

    @Test
    void testCustomerRequestToCustomer() {

        // Arrange
        CustomerRequest customerRequest = CustomerRequestFactory.randomCustomerRequest().build();

        // Act
        Customer customer = CustomerMapper.CustomerRequestToCustomer(customerRequest);

        // Assert
        assertEquals(customerRequest.username(), customer.getUsername());
        assertEquals(customerRequest.name(), customer.getName());
        assertEquals(customerRequest.address(), customer.getAddress());
        assertEquals(customerRequest.email(), customer.getEmail());
    }
}

