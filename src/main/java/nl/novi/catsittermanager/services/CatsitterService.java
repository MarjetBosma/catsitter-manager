package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatsitterService {

    private final CatsitterRepository catsitterRepository;

    public List<Catsitter> getAllCatsitters() {
        return catsitterRepository.findAll();
    }

    public Catsitter getCatsitter(final String username) {
        return catsitterRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException("No catsitter found with this username."));
    }

    public List<Order> getAllOrdersByCatsitter(String username) {
        Catsitter catsitter = getCatsitter(username);
        return catsitter.getOrders();
    }

    public List<Customer> getAllCustomersByCatsitter(String username) {
        Catsitter catsitter = getCatsitter(username);
        List<Order> orders = catsitter.getOrders();
        List<Customer> customers = orders.stream()
                .map(Order::getCustomer)
                .distinct()
                .toList();
        return customers;
    }

    public Catsitter createCatsitter(final Catsitter catsitter) {
        if (catsitterRepository.findById(catsitter.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(catsitter.getPassword());
        catsitter.setPassword(encodedPassword);
        catsitter.setEnabled(true);
        catsitter.setRole(Role.CATSITTER);
        catsitter.setOrders(new ArrayList<>());
        return catsitterRepository.save(catsitter);
    }

    public Catsitter editCatsitter(final String username, final Catsitter catsitter) {
        if (catsitterRepository.findById(username).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username.");
        }
        return catsitterRepository.save(catsitter);
    }

    public String deleteCatsitter(final String username) {
        if (!catsitterRepository.existsById(username)) {
            throw new RecordNotFoundException("No catsitter found with this username.");
        }
        catsitterRepository.deleteById(username);
        return username;
    }
}






