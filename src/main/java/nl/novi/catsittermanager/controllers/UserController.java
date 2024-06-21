package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.mappers.UserMapper;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponseList = userService.getAllUsers().stream()
                .map(UserMapper::UserToUserResponse)
                .toList();
        return ResponseEntity.ok(userResponseList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") final String username) throws URISyntaxException {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserMapper.UserToUserResponse(user));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createAdminAccount(@Valid @RequestBody final UserRequest userRequest) {
        User user = userService.createAdminAccount(UserMapper.UserRequestToUser(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.UserToUserResponse(user));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> editUser(@Valid @PathVariable("id") final String username, @Valid @RequestBody final UserRequest userRequest) {
        User user = userService.editUser(username, UserMapper.UserRequestToUser(userRequest));
        return ResponseEntity.ok().body(UserMapper.UserToUserResponse(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") final String userToDelete) {
        userService.deleteUser(userToDelete);
        return ResponseEntity.ok().body("User with username " + userToDelete + " removed from database.");
    }
}
