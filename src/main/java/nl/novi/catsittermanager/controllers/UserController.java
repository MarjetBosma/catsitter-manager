package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.UserService;
import nl.novi.catsittermanager.services.UserServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

//@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserServiceImplementation userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") final UUID idToFind) {
        return ResponseEntity.ok(userService.getUser(idToFind));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody final UserInputDto userInputDto, final BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(bindingResult));
        } else {
            UserDto savedUser = userService.createUser(userInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedUser).toUriString());
            return ResponseEntity.created(uri).body(savedUser);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable("id") final UUID idToEdit, @RequestBody final UserInputDto user) {
        return ResponseEntity.ok().body(userService.editUser(idToEdit, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UUID idToDelete) {
        userService.deleteUser(idToDelete);
        return ResponseEntity.ok().body("User with id " + idToDelete + " removed from database");
    }
}
