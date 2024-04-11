package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import org.springframework.http.HttpStatus;
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

    public Catsitter createCatsitter(final Catsitter catsitter) {
        if (catsitterRepository.findById(catsitter.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        catsitter.setEnabled(true);
        catsitter.setRole(Role.CATSITTER);
        catsitter.setOrders(new ArrayList<Order>());
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






