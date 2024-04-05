package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.user.UserResponse;
import nl.novi.catsittermanager.dtos.user.UserRequest;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
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

//@CrossOrigin
@RestController
@RequestMapping(value = "/user")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponseList = userService.getAllUsers().stream()
                .map(UserMapper::UserToUserResponse)
                .toList();
        return ResponseEntity.ok(userResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") final String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserMapper.UserToUserResponse(user));
    }

    // todo: uitzoeken waarom de extra parameter username in de service hier een probleem geeft, en of ik die username validatie wellicht ergens anders moet doen
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody final UserRequest userRequest) {
        User user = userService.createUser(UserMapper.UserRequestToUser(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.UserToUserResponse(user));
    }

    // todo: Beslissen of ik onderstaande versie met optie voor validation exception wil implementeren

//    @PostMapping
//    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody final UserRequest userRequest, final BindingResult br) {
//        if (br.hasFieldErrors()) {
//            throw new ValidationException(checkForBindingResult(br));
//        } else {
//            User user = userService.createUser(UserMapper.UserRequestToUser(userRequest));
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder
//                            .fromCurrentRequest()
//                            .path("/" + user).toUriString());
//            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.UserToUserResponse(user));
//        }
//    }

    // todo: uitzoeken waarom deze een authentication error geeft, getest nadat ik deels de implementatie van security heb gedaan
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") final String username, @RequestBody final UserRequest userRequest) {
        User user = userService.editUser(username, UserMapper.UserRequestToUser(userRequest));
        return ResponseEntity.ok().body(UserMapper.UserToUserResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") final String userToDelete) {
        userService.deleteUser(userToDelete);
        return ResponseEntity.ok().body("User with id " + userToDelete + " removed from database");
    }

}
