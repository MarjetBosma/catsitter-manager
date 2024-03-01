package nl.novi.catsittermanager.dtos.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.catsittermanager.enumerations.Role;
//import nl.novi.catsittermanager.models.Authority;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public record UserInputDto (

    Long id,  //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
    @NotNull(message = "username is required")
    String username,
    @NotNull(message = "password is required")
    String password,
    Role role,

//    @JsonSerialize
//    Set<Authority> authorities,
    String authorities, // Dummy, alleen voor los testen Cat class zonder database
    Boolean enabled,
    String name,
    String address,
    String email

) {}
