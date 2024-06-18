package nl.novi.catsittermanager.services;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameAlreadyExistsException;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    public User createAdminAccount(final User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
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

