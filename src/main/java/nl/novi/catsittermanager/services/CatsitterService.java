package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.Catsitter;
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
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username."));
    }

    public List<Order> getAllOrdersByCatsitter(String username) {
        Catsitter catsitter = getCatsitter(username);
        return catsitter.getOrders();
    }

    public Catsitter createCatsitter(final Catsitter catsitter) {
        if (catsitterRepository.findById(catsitter.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(catsitter.getUsername());
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(catsitter.getPassword());
        catsitter.setPassword(encodedPassword);
        catsitter.setEnabled(true);
        catsitter.setRole(Role.CATSITTER);
        catsitter.setOrders(new ArrayList<>());
        return catsitterRepository.save(catsitter);
    }

    public Catsitter editCatsitter(final String username, final Catsitter updatedCatsitter) {
        Catsitter existingCatsitter = catsitterRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catsitter found with this username."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(updatedCatsitter.getPassword());

        existingCatsitter.setUsername(updatedCatsitter.getUsername());
        existingCatsitter.setPassword(encodedPassword);
        existingCatsitter.setName(updatedCatsitter.getName());
        existingCatsitter.setAddress(updatedCatsitter.getAddress());
        existingCatsitter.setEmail(updatedCatsitter.getEmail());
        existingCatsitter.setAbout(updatedCatsitter.getAbout());

        return catsitterRepository.save(updatedCatsitter);
    }

    public String deleteCatsitter(final String username) {
        if (!catsitterRepository.existsById(username)) {
            throw new RecordNotFoundException("No catsitter found with this username.");
        }
        catsitterRepository.deleteById(username);
        return username;
    }
}






