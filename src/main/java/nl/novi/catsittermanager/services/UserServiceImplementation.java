package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.enumerations.Role;
//import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
import nl.novi.catsittermanager.mappers.UserMapper;
//import nl.novi.catsittermanager.models.Authority;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.utils.RandomStringGenerator;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

//    private final UserRepository userRepository;

//    private PasswordEncoder encoder;

    private List<User> users = new ArrayList<>(); // voor testen zonder database

//    public UserServiceImplementation(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public UserServiceImplementation() { // voor testen zonder database
        users.add(new User(1L, "HannahD", "Firsa2006", Role.USER, "authorities", false, "Hannah Daalder", "Kerkstraat 3, Ergenshuizen", "hannahdaalder@gmail.com" ));
        users.add(new User(2L, "Catlover", "Cats4Ever", Role.ADMIN, "authorities", true, "Pietje Puk", "Straatweg 45 Grotestad", "pietjepuk@gmail.com"));
    }
    @Override
    public List<UserDto> getAllUsers() {
//        List<User> userDtoList = userRepos.findAll(); // Deze is voor als de database gevuld is
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = UserMapper.transferToDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto getUser(long idToFind) {
        for (User user : users) {
            if (user.getId() == idToFind) {
                return UserMapper.transferToDto(user);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id.");
    }

    @Override
    public UserDto createUser(UserInputDto userInputDto) {
        User newUser = UserMapper.transferFromDto(userInputDto);
        users.add(newUser);
        return UserMapper.transferToDto(newUser);
    }
    @Override
    public UserDto editUser(long idToEdit, UserInputDto userInputDto) {
        for (User user : users) {
            if (user.getId() == idToEdit) {
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
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id");
    }

        @Override
        public long deleteUser (long idToDelete) {
            for (User user : users) {
                if (user.getId() == idToDelete) {
                    users.remove(user);
                    return idToDelete;
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this id.");
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
