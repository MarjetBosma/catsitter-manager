package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatFactory;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.CustomerFactory;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        verify(customerRepository, times(1)).save(expectedCustomer);
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
    void testGetCat_shouldFetchCatWithSpecificId_RecordNotFoundException() {
        // Given
        Customer expectedCustomer = CustomerFactory.randomCustomer().build();

        when(customerRepository.findById(expectedCustomer.getUsername())).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception=assertThrows(RecordNotFoundException.class, () -> customerService.getCustomer(expectedCustomer.getUsername()));

        // Then
        assertEquals("No customer found with this id.", exception.getMessage());
    }

    @Test
    void testGetAllCatsByCustomer_shouldFetchAllCatsForThisCustomer() {
        // Given
        String username = "testUser";
        Customer customer = CustomerFactory.randomCustomer().build();
        List<Cat> expectedCats = CatFactory.randomCats(3);
        customer.setCats(expectedCats);

        when(customerService.getCustomer(username)).thenReturn(customer);

        // When
        List<Cat> resultCats = customerService.getAllCatsByCustomer(username);

        // Then
        assertEquals(expectedCats.size(), resultCats.size());
        assertTrue(resultCats.containsAll(expectedCats));

        verify(customerRepository, times(1)).findById(username);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllCatsByCustomer_noCatsOnTheList_shouldReturnEmptyList() {
        // Given
        String username = "testUser";
        when(customerService.getCustomer(username)).thenReturn(CustomerFactory.randomCustomer());

        // When
        List<Cat> resultCats = customerService.getAllCatsByCustomer(username);

        // Then
        assertNotNull(resultCats);
        assertTrue(resultCats.isEmpty());

        verify(customerService, times(1)).getCustomer(username);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testGetAllOrdersByCustomer_shouldFetchAllOrdersForThisCustomer() {
        // Given
        String username = "testUser";
        Customer customer = CustomerFactory.randomCustomer().build();
        List<Order> expectedOrders = OrderFactory.randomOrders(3);
        customer.setOrders(expectedOrders);

        when(customerService.getCustomer(username)).thenReturn(customer);

        // When
        List<Order> resultOrders = customerService.getAllOrdersByCustomer(username);

        // Then
        assertEquals(expectedOrders.size(), resultOrders.size());
        assertTrue(resultOrders.containsAll(expectedOrders));

        verify(customerRepository, times(1)).findById(username);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetAllOrdersByCustomer_noOrdersOnTheList_shouldReturnEmptyList() {
        // Given
        String username = "testUser";
        when(customerService.getCustomer(username)).thenReturn(CustomerFactory.randomCustomer());

        // When
        List<Order> resultOrders = customerService.getAllOrdersByCustomer(username);

        assertNotNull(resultOrders);
        assertTrue(resultOrders.isEmpty());

        verify(customerService, times(1)).getCustomer(username);
        verifyNoMoreInteractions(customerService);

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
        String username = customerService.deleteCustomer(customer.getUsername());

        // Then
        verify(customerRepository, times(1)).existsById(username);
        verifyNoMoreInteractions(customerRepository);
    }
}