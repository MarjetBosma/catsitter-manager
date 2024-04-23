package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.mappers.UserMapper;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.helpers.ControllerHelper.checkForBindingResult;

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
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") final String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserMapper.UserToUserResponse(user));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createAdminAccount(@Valid @RequestBody final UserRequest userRequest, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            User user = userService.createAdminAccount(UserMapper.UserRequestToUser(userRequest));
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/user")
                            .toUriString());
            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.UserToUserResponse(user));
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") final String username, @RequestBody final UserRequest userRequest) {
        User user = userService.editUser(username, UserMapper.UserRequestToUser(userRequest));
        return ResponseEntity.ok().body(UserMapper.UserToUserResponse(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") final String userToDelete) {
        userService.deleteUser(userToDelete);
        return ResponseEntity.ok().body("User with id " + userToDelete + " removed from database");
    }
}
