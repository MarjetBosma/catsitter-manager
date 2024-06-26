package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(final String username) {
        return customerRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username."));
    }

    public List<Cat> getAllCatsByCustomer(String username) {
        Customer customer = getCustomer(username);
        return customer.getCats();
    }

    public List<Order> getAllOrdersByCustomer(String username) {
        Customer customer = getCustomer(username);
        return customer.getOrders();
    }

    public Customer createCustomer(final Customer customer) {
        if (customerRepository.findById(customer.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(customer.getUsername());
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customer.setEnabled(true);
        customer.setRole(Role.CUSTOMER);
        customer.setOrders(new ArrayList<>());
        customer.setCats(new ArrayList<>());
        return customerRepository.save(customer);
    }

    public Customer editCustomer(final String username, final Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No customer found with this username."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(updatedCustomer.getPassword());

        existingCustomer.setUsername(updatedCustomer.getUsername());
        existingCustomer.setPassword(encodedPassword);
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepository.save(updatedCustomer);
    }

    public String deleteCustomer(final String username) {
        if (!customerRepository.existsById(username)) {
            throw new RecordNotFoundException("No customer found with this username.");
        }
        customerRepository.deleteById(username);
        return username;
    }
}
