package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.UserServiceImplementation;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

//@CrossOrigin
@RestController
@RequestMapping(value = "/user")

public class UserController {

    private final UserServiceImplementation userService;

    public UserController(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") long idToFind) {
        if (idToFind > 0) {
            UserDto userDto = userService.getUser(idToFind);
            return ResponseEntity.ok(userDto);
        } else {
            throw new RecordNotFoundException("No user found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserInputDto userInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
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
    public ResponseEntity<UserDto> editUser(@PathVariable("id") long idToEdit, @RequestBody UserInputDto user) {
        UserDto editedUser = userService.editUser(idToEdit, user);
        return ResponseEntity.ok().body(editedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long idToDelete) {
        userService.deleteUser(idToDelete);
        return ResponseEntity.ok().body("User with id " + idToDelete +  " removed from database");
    }

//    @GetMapping(value = "")
//    public ResponseEntity<List<UserDto>> getAllUsers() {
//
//        List<UserDto> userDtos = userService.getUsers();
//
//        return ResponseEntity.ok().body(userDtos);
//    }
//
//    @GetMapping(value = "/{username}")
//    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {
//
//        UserDto optionalUser = userService.getUser(username);
//
//
//        return ResponseEntity.ok().body(optionalUser);
//
//    }
//
//    @PostMapping(value = "")
//    public ResponseEntity<UserDto> createKlant(@RequestBody UserDto dto) {
//
//        String newUsername = userService.createUser(dto);
//        userService.addAuthority(newUsername, "ROLE_USER");
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
//                .buildAndExpand(newUsername).toUri();
//
//        return ResponseEntity.created(location).build();
//    }
//
//    @PutMapping(value = "/{username}")
//    public ResponseEntity<UserDto> updateKlant(@PathVariable("username") String username, @RequestBody UserDto dto) {
//
//        userService.updateUser(username, dto);
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping(value = "/{username}")
//    public ResponseEntity<Object> deleteKlant(@PathVariable("username") String username) {
//        userService.deleteUser(username);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
//        return ResponseEntity.ok().body(userService.getAuthorities(username));
//    }
//
//    @PostMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> addUserAuthority(
//            @PathVariable("username") String username,
//            @RequestBody AuthorityDto authorityDto) {
//        try {
//            String authorityName = authorityDto.authority;
//            userService.addAuthority(username, authorityName);
//            return ResponseEntity.noContent().build();
//        } catch (Exception ex) {
//            throw new BadRequestException();
//        }
}
