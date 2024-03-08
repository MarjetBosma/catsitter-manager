package nl.novi.catsittermanager.dtos.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.catsittermanager.enumerations.Role;
//import nl.novi.catsittermanager.models.Authority;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.UUID;

@Validated
public record UserInputDto (

        UUID id,  //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
        @NotNull(message = "username is required")
    String username,
        @NotNull(message = "password is required")
    String password,
        Role role,
//    @JsonSerialize
//    Set<Authority> authorities,
        String authorities, // Dummy
        Boolean enabled,
        String name,
        String address,
        String email

) {}
