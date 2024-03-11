package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
//import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.UserMapper;
//import nl.novi.catsittermanager.models.Authority;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepos;

    public UserServiceImplementation(UserRepository userRepos) {
        this.userRepos = userRepos;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepos.findAll().stream()
                .map(UserMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(UUID idToFind) {
        return userRepos.findById(idToFind)
                .map(UserMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    @Override
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
        public UUID deleteUser (UUID idToDelete) {
            userRepos.deleteById(idToDelete);
            return idToDelete;
        }

//    public List<UserDto> getUsers() {
//        List<UserDto> collection = new ArrayList<>();
//        List<User> list = userRepository.findAll();
//        for (User user : list) {
//            collection.add(fromUser(user));
//        }
//        return collection;
//    }
//
//    public UserDto getUser(String username) {
//        UserDto dto = new UserDto();
//        Optional<User> user = userRepository.findById(username);
//        if (user.isPresent()){
//            dto = fromUser(user.get());
//        }else {
//            throw new UsernameNotFoundException(username);
//        }
//        return dto;
//    }
//
//    public boolean userExists(String username) {
//        return userRepository.existsById(username);
//    }
//
//    public String createUser(UserDto userDto) {
//        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
//        encoder.encode(userDto.password);
//        User newUser = userRepository.save(toUser(userDto));
//        return newUser.getUsername();
//    }
//
//    public void deleteUser(String username) {
//        userRepository.deleteById(username);
//    }
//
//    public void updateUser(String username, UserDto newUser) {
//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
//        user.setPassword(newUser.password);
//        userRepository.save(user);
//    }
//
//    public Set<Authority> getAuthorities(String username) {
//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
//        UserDto userDto = fromUser(user);
//        return userDto.authorities;
//    }
//
//    public void addAuthority(String username, String authority) {
//
//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
//        user.addAuthority(new Authority(username, authority));
//        userRepository.save(user);
//    }
//
//    public void removeAuthority(String username, String authority) {
//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
//        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
//        user.removeAuthority(authorityToRemove);
//        userRepository.save(user);
//    }
    }
