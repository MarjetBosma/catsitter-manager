package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.mappers.UserMapper;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepos;

    public UserServiceImplementation(UserRepository userRepos) {
        this.userRepos = userRepos;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepos.findAll();

        for (User user : userList) {
            UserDto userDto = UserMapper.transferToDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto getUser(UUID idToFind) {
        Optional<User> userOptional = userRepos.findById(idToFind);
        if (userOptional.isPresent()) {
            return UserMapper.transferToDto(userOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id.");
        }
    }

    @Override
    public UserDto createUser(@RequestBody UserInputDto userInputDto) {
        User newUser = new User();
        newUser.setUsername(userInputDto.username());
        newUser.setPassword(userInputDto.password());
        newUser.setEmail(userInputDto.email());
        newUser.setRole(userInputDto.role());
        newUser.setAuthorities(userInputDto.authorities());
        newUser.setEnabled(userInputDto.enabled());
        newUser.setName(userInputDto.name());
        newUser.setAddress(userInputDto.address());
        newUser.setEmail(userInputDto.email());
        userRepos.save(newUser);
        return UserMapper.transferToDto(newUser);
    }

    @Override
    public UserDto editUser(UUID idToEdit, UserInputDto userInputDto) {
        Optional<User> optionalUser = userRepos.findById(idToEdit);

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id");
        }
    }

    @Override
    public UUID deleteUser(UUID idToDelete) {
        Optional<User> optionalUser = userRepos.findById(idToDelete);
        if (optionalUser.isPresent()) {
            userRepos.deleteById(idToDelete);
            return idToDelete;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id.");
        }
    }
}
