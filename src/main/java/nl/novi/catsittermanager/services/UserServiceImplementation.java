//package nl.novi.catsittermanager.services;
//
//import nl.novi.catsittermanager.dtos.user.UserDto;
//import nl.novi.catsittermanager.dtos.user.UserInputDto;
//import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
//import nl.novi.catsittermanager.mappers.UserMapper;
//import nl.novi.catsittermanager.models.Authority;
//import nl.novi.catsittermanager.models.User;
//import nl.novi.catsittermanager.repositories.UserRepository;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class UserServiceImplementation implements UserService {
//
//    private final UserRepository userRepos;
//
//    private final PasswordEncoder encoder;
//
//    public UserServiceImplementation(UserRepository userRepos, PasswordEncoder encoder) {
//        this.userRepos = userRepos;
//        this.encoder = encoder;
//    }
//
//    @Override
//    public List<UserDto> getAllUsers() {
//        return userRepos.findAll().stream()
//                .map(UserMapper::transferToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public UserDto getUser(String username) {
//        return userRepos.findById(username)
//                .map(UserMapper::transferToDto)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this username."));
//    }
//
//    @Override
//    public UserDto createUser(@RequestBody UserInputDto userInputDto) {
//        encoder.encode(userInputDto.password());
//        User user = User.builder()
//                .username(userInputDto.username())
//                .password(userInputDto.password())
//                .email(userInputDto.email())
//                .role(userInputDto.role())
//                .authorities(userInputDto.authorities())
//                .enabled(userInputDto.enabled())
//                .name(userInputDto.name())
//                .address(userInputDto.address())
//                .email(userInputDto.email())
//                .build();
//        userRepos.save(user);
//        return UserMapper.transferToDto(user);
//    }
//
//    @Override
//    public UserDto editUser(String username, UserInputDto userInputDto) {
//        Optional<User> optionalUser = userRepos.findById(username);
//
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                if (userInputDto.username() != null) {
//                    user.setUsername(userInputDto.username());
//                }
//                if (userInputDto.password() != null) {
//                    user.setPassword(userInputDto.password());
//                }
//                if (userInputDto.email() != null) {
//                    user.setEmail(userInputDto.email());
//                }
//                if (userInputDto.role() != null) {
//                    user.setRole(userInputDto.role());
//                }
//                if (userInputDto.authorities() != null) {
//                    user.setAuthorities(userInputDto.authorities());
//                }
//                if (userInputDto.enabled() != null) {
//                    user.setEnabled(userInputDto.enabled());
//                }
//                if (userInputDto.name() != null) {
//                    user.setName(userInputDto.name());
//                }
//                if (userInputDto.address() != null) {
//                    user.setAddress(userInputDto.address());
//                }
//                userRepos.save(user);
//                return UserMapper.transferToDto(user);
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with this username");
//        }
//    }
//
//    @Override
//    public String deleteUser (String username) {
//        userRepos.deleteById(username);
//        return username;
//    }
//
//    public Set<Authority> getAuthorities(String username) {
//        if (!userRepos.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepos.findById(username).get();
//        UserDto userDto = UserMapper.transferToDto(user);
//        return userDto.authorities();
//    }
//
//    public void addAuthority(String username, String authority) {
//        if (!userRepos.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepos.findById(username).get();
//        user.addAuthority(new Authority(username, authority));
//        userRepos.save(user);
//    }
//
//    public void removeAuthority(String username, String authority) {
//        if (!userRepos.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepos.findById(username).get();
//        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
//        user.removeAuthority(authorityToRemove);
//        userRepos.save(user);
//    }
//}
