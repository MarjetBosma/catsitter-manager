package nl.novi.catsittermanager.dtos.user;

import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.enumerations.Role;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public record UserInputDto(

        UUID id,  //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
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

) {
}
