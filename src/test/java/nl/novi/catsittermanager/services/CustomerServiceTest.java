package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    void testGetAllCustomers_shouldFetchAllCustomersOnTheList() {
        // Given
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();
        List<Customer> expectedCustomerList = List.of(expectedCustomer);

        when(customerRepository.findAll()).thenReturn(expectedCustomerList);

        // When
        List<Customer> customerResponseList = customerService.getAllCustomers();

        // Then
        assertEquals(expectedCustomerList, customerResponseList);

        verify(customerRepository, times(1)).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllCustomers_noCustomersInDatabase_shouldReturnEmptyList() {
        // Given
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Customer> catResponseList = customerService.getAllCustomers();

        // Then
        assertTrue(catResponseList.isEmpty());
        verify(customerRepository, times(1)).findAll();
        verifyNoMoreInteractions(customerRepository);
    }
    @Test
    void testGetCustomer_shouldFetchCustomerWithSpecificUsername() {
        // Given
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerRepository.findById(expectedCustomer.getUsername())).thenReturn(Optional.of(expectedCustomer));

        // When
        Customer resultCustomer = customerService.getCustomer(expectedCustomer.getUsername());

        // Then
        assertEquals(expectedCustomer, resultCustomer);
        verify(customerRepository, times(1)).findById(expectedCustomer.getUsername());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetCustomer_shouldFetchCustomerWithSpecificUsername_RecordNotFoundException() {
        // Given
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerRepository.findById(expectedCustomer.getUsername())).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception=assertThrows(RecordNotFoundException.class, () -> customerService.getCustomer(expectedCustomer.getUsername()));

        // Then
        assertEquals("No customer found with this username.", exception.getMessage());
    }

    @Test
    void testGetAllCatsByCustomer_shouldFetchAllCatsForThisCustomer() {
        // Given
        Customer randomCustomer = CustomerFactory.randomCustomer().build();
        List<Cat> expectedCats = CatFactory.randomCats(3);
        randomCustomer.setCats(expectedCats);

        when(customerRepository.findById(randomCustomer.getUsername())).thenReturn(Optional.of(randomCustomer));

        // When
        List<Cat> resultCats = customerService.getAllCatsByCustomer(randomCustomer.getUsername());

        // Then
        assertEquals(expectedCats.size(), resultCats.size());
        assertTrue(resultCats.containsAll(expectedCats));

        verify(customerRepository, times(1)).findById(randomCustomer.getUsername());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllCatsByCustomer_noCatsOnTheList_shouldReturnEmptyList() {
        // Given
        Customer randomCustomer = CustomerFactory.randomCustomer()
                .cats(new ArrayList<Order>())
                .build();

        when(customerRepository.findById(randomCustomer.getUsername())).thenReturn(Optional.of(randomCustomer));

        // When
        List<Cat> resultCats = customerService.getAllCatsByCustomer(randomCustomer.getUsername());

        // Then
        assertNotNull(resultCats);
        assertTrue(resultCats.isEmpty());

        verify(customerRepository, times(1)).findById(randomCustomer.getUsername());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllOrdersByCustomer_shouldFetchAllOrdersForThisCustomer() {
        // Given
        Customer randomCustomer = CustomerFactory.randomCustomer().build();
        List<Order> expectedOrders = OrderFactory.randomOrders(3);
        randomCustomer.setOrders(expectedOrders);

        when(customerRepository.findById(randomCustomer.getUsername())).thenReturn(Optional.of(randomCustomer));

        // When
        List<Order> resultOrders = customerService.getAllOrdersByCustomer(randomCustomer.getUsername());

        // Then
        assertEquals(expectedOrders.size(), resultOrders.size());
        assertTrue(resultOrders.containsAll(expectedOrders));

        verify(customerRepository, times(1)).findById(randomCustomer.getUsername());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllOrdersByCustomer_noOrdersOnTheList_shouldReturnEmptyList() {
        // Given
        Customer randomCustomer = CustomerFactory.randomCustomer()
                .orders(new ArrayList<Order>())
                .build();

        when(customerRepository.findById(randomCustomer.getUsername())).thenReturn(Optional.of(randomCustomer));

        // When
        List<Order> resultOrders = customerService.getAllOrdersByCustomer(randomCustomer.getUsername());

        assertNotNull(resultOrders);
        assertTrue(resultOrders.isEmpty());

        verify(customerRepository, times(1)).findById(randomCustomer.getUsername());
        verifyNoMoreInteractions(customerRepository);

    }

    @Test
    void testCreateCustomer_shouldCreateANewCustomer() {
        // Given
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerRepository.save(expectedCustomer)).thenReturn(expectedCustomer);

        // When
        Customer resultCustomer = customerService.createCustomer(expectedCustomer);

        // Then
        assertEquals(expectedCustomer, resultCustomer);

        verify(customerRepository, times(1)).save(expectedCustomer);
    }

    @Test
    void testCreateCustomer_WhenUsernameExists_shouldThrowUsernameAlreadyExistsException() {
        // Given
        Customer existingCustomer = CustomerFactory.randomCustomer().build();
        String existingUsername = "existingUsername";
        existingCustomer.setUsername(existingUsername);

        when(customerRepository.findById(existingUsername)).thenReturn(Optional.of(existingCustomer));

        // When & Then
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            customerService.createCustomer(existingCustomer);
        });
    }

    @Test
    void testEditCustomer_shouldEditExistingCustomer() {
        // Given
        Customer customer = CustomerFactory.randomCustomer().build();

        when(customerRepository.findById(customer.getUsername())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        Customer resultCustomer = customerService.editCustomer(customer.getUsername(), customer);

        // Then
        assertEquals(customer, resultCustomer);

        verify(customerRepository, times(1)).findById(customer.getUsername());
        verify(customerRepository, times(1)).save(customer);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testEditCustomer_nonExistingCustomer_shouldThrowRecordNotFoundException() {
        // Given
        Customer customer = CustomerFactory.randomCustomer().build();

        when(customerRepository.findById(customer.getUsername())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> customerService.editCustomer(customer.getUsername(), customer));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testDeleteCustomer_shouldDeleteCustomerWithSpecificId() {
        // Given
        Customer customer = CustomerFactory.randomCustomer().build();
        when(customerRepository.existsById(customer.getUsername())).thenReturn(true);

        // When
        String resultUsername = customerService.deleteCustomer(customer.getUsername());

        // Then
        verify(customerRepository, times(1)).existsById(customer.getUsername());
        verify(customerRepository, times(1)).deleteById(customer.getUsername());
        verifyNoMoreInteractions(customerRepository);

        assertEquals(customer.getUsername(), resultUsername);
    }
}