package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.UserMapper;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepos;

    public List<UserDto> getAllUsers() {
        return userRepos.findAll().stream()
                .map(UserMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(String usernameToFind) {
        return userRepos.findById(usernameToFind)
                .map(UserMapper::transferToDto)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username."));
    }

    public UserDto createUser(@RequestBody UserInputDto userInputDto) {
        User user = User.builder()
                .username(userInputDto.username())
                .password(userInputDto.password())
                .role(userInputDto.role())
                .authorities(userInputDto.authorities())
                .enabled(userInputDto.enabled())
                .name(userInputDto.name())
                .address(userInputDto.address())
                .email(userInputDto.email())
                .build();
        userRepos.save(user);
        return UserMapper.transferToDto(user);
    }

    public UserDto editUser(String userToEdit, UserInputDto userInputDto) {
        Optional<User> optionalUser = userRepos.findById(userToEdit);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userInputDto.username() != null) {
                user.setUsername(userInputDto.username());
            }
            if (userInputDto.password() != null) {
                user.setPassword(userInputDto.password());
            }
            if (userInputDto.role() != null) {
                user.setRole(userInputDto.role());
            }
            if (userInputDto.authorities() != null) {
                user.setAuthorities(userInputDto.authorities());
            }
            if (userInputDto.enabled() != null) {
                user.setEnabled(userInputDto.enabled());
            }
            if (userInputDto.name() != null) {
                user.setName(userInputDto.name());
            }
            if (userInputDto.address() != null) {
                user.setAddress(userInputDto.address());
            }
            if (userInputDto.email() != null) {
                user.setEmail(userInputDto.email());
            }
            userRepos.save(user);
            return UserMapper.transferToDto(user);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No user found with this username");
        }
    }

    public String deleteUser(String userToDelete) {
        userRepos.deleteById(userToDelete);
        return userToDelete;
    }

}
