//package nl.novi.catsittermanager.controllers;
//
//import nl.novi.catsittermanager.dtos.authority.AuthorityDto;
//import nl.novi.catsittermanager.dtos.user.UserDto;
//import nl.novi.catsittermanager.dtos.user.UserInputDto;
//import nl.novi.catsittermanager.exceptions.BadRequestException;
//import nl.novi.catsittermanager.exceptions.ValidationException;
//import nl.novi.catsittermanager.services.UserServiceImplementation;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;
//
//@CrossOrigin
//@RestController
//@RequestMapping(value = "/user")
//
//public class UserController {
//
//    private final UserServiceImplementation userService;
//
//    public UserController(UserServiceImplementation userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserDto>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> getUser(@PathVariable("id") final String email) {
//            UserDto userDto = userService.getUser(email);
//            return ResponseEntity.ok(userDto);
//    }
//
//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@RequestBody final UserInputDto userInputDto, final BindingResult br) {
//        if (br.hasFieldErrors()) {
//            throw new ValidationException(checkForBindingResult(br));
//        } else {
//            UserDto savedUser = userService.createUser(userInputDto);
//            userService.addAuthority(savedUser.email(), "ROLE_USER");
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder
//                            .fromCurrentRequest()
//                            .path("/" + savedUser).toUriString());
//            return ResponseEntity.created(uri).body(savedUser);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> editUser(@PathVariable("id") final String email, @RequestBody final UserInputDto user) {
//        UserDto editedUser = userService.editUser(email, user);
//        return ResponseEntity.ok().body(editedUser);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteUser(@PathVariable("id") final String email) {
//        userService.deleteUser(email);
//        return ResponseEntity.ok().body("User with id " + email +  " removed from database");
//    }
//
//    @GetMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> getAuthorities(@PathVariable("username") String email) {
//        return ResponseEntity.ok().body(userService.getAuthorities(email));
//    }
//
//    @PostMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> addAuthority(@PathVariable("username") String username, @RequestBody AuthorityDto authorityDto) {
//        try {
//            String authorityName = authorityDto.authority;
//            userService.addAuthority(username, authorityName);
//            return ResponseEntity.noContent().build();
//        } catch (Exception ex) {
//            throw new BadRequestException();
//        }
//    }
//
//    @DeleteMapping(value = "/{username}/authorities/{authority}")
//    public ResponseEntity<Object> removeAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
//        userService.removeAuthority(username, authority);
//        return ResponseEntity.noContent().build();
//    }
//
//}
