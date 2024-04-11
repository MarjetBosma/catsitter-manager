package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(final String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username."));
    }

    // todo: ik heb hier nu alleen een methode voor admin aanmaken, customer en catsitter aanmaken staat in hun eigen services. Is dit de beste optie?
    public User createAdminAccount(final User user) { // todo: apart admin entity of request maken?
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        String hashedPassword = passwordEncoderService.hashPassword(user.getPassword());
        // user.getAuthorities().add(); // Wat zijn nu precies de authorities t.o.v. de role?
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return userRepository.save(user);
    }

    public User editUser(final String username, final User user) {
        if (userRepository.findById(username).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username.");
        }
        return userRepository.save(user);
    }
//     todo: deze geeft een authentication error, waarom?

    public String deleteUser(final String username) {
        if (!userRepository.existsById(username)) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username.");
        }
        userRepository.deleteById(username);
        return username;
    }
}

