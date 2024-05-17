package nl.novi.catsittermanager.dtos.user;

import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.enumerations.Role;
import org.springframework.validation.annotation.Validated;


@Validated
public record UserRequest(

        @NotNull(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        String password,
        Role role,
        Boolean enabled,
        @NotNull(message = "name is required")
        String name,
        @NotNull(message = "address is required")
        String address,
        @NotNull(message = "email is required")
        String email

) {
}
