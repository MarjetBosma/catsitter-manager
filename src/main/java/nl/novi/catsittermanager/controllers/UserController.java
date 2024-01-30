package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.user.UserDto;
import nl.novi.catsittermanager.dtos.user.UserResponseDto;
import nl.novi.catsittermanager.exceptions.BadRequestException;
import nl.novi.catsittermanager.dtos.authority.AuthorityDto;
import nl.novi.catsittermanager.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getUsers() {

        List<UserDto> userDtos = userService.getUsers();

        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {

        UserDto optionalUser = userService.getUser(username);


        return ResponseEntity.ok().body(optionalUser);

    }

    @PostMapping(value = "")
    public ResponseEntity<UserDto> createKlant(@RequestBody UserDto dto) {

        String newUsername = userService.createUser(dto);
        userService.addAuthority(newUsername, "ROLE_USER");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<UserDto> updateKlant(@PathVariable("username") String username, @RequestBody UserDto dto) {

        userService.updateUser(username, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteKlant(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

    // Vraag in to do: Als Requestbody wordt hier een Map<String, Object> gebruikt om de "authorityName" binnen te halen, dat werkt, maar kun je een betere oplossing bedenken?
    // Antwoord: Ja, met een DTO. Ik heb deze class AuthorityDto genoemd en ondergebracht in het mapje dtos/authority,

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(
            @PathVariable("username") String username,
            @RequestBody AuthorityDto authorityDto) {
        try {
            String authorityName = authorityDto.authority;
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    // Oude versie:

//    @PostMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
//        try {
//            String authorityName = (String) fields.get("authority");
//            userService.addAuthority(username, authorityName);
//            return ResponseEntity.noContent().build();
//        }
//        catch (Exception ex) {
//            throw new BadRequestException();
//        }
//    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

    // Bonusopdracht
    // Toepassen van SecurityContextHolder en Authentication classes (zie imports), en methode getUserById toegevoegd.
    // Ik heb een aparte ResponseDto gemaakt, die een beperkt aantal velden teruggeeft aan de gebruiker.
    // Ben er niet meer aan toegekomen om dit uit te testen in Postman voor de deadline, maar dit wil ik nog wel gaan doen. Ben wel benieuwd of ik op de goede weg zit.

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals(id)) {
            UserDto userDto = userService.getUser(id);
            UserResponseDto responseDto = new UserResponseDto(userDto.username, userDto.email, userDto.authorities);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}