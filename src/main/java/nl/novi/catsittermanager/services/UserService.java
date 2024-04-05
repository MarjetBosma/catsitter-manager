package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.mappers.UserMapper;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(final String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username."));
    }

    // todo: Uitzoeken waarom de extra parameter username een probleem geeft in de controller
    public User createUser(final User user, final String username) {
        if (userRepository.existsById(username)) {
            throw new UsernameAlreadyExistsException("Username " + username + " already exists. Please log in or choose another username to create a new account.");
        }
        user.setEnabled(true);
        // user.setRole(Role.CUSTOMER);
        // todo: Bepalen waar bovenstaande moet. Er kan ook een customer of catsitter aangemaakt worden waar de rol (CUSTOMER of CATSITTER wordt gezet. Er moet echter ook een ADMIN aangemaakt kunnen worden.
        return userRepository.save(user);
    }

    public User editUser(final String username, final User user) {
        if (userRepository.findById(username).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username.");
        }
        return userRepository.save(user);
    }

    public String deleteUser(final String username) {
        if (!userRepository.existsById(username)) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username.");
        }
        userRepository.deleteById(username);
        return username;
    }
}

