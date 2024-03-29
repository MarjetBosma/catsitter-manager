package nl.novi.catsittermanager.dtos.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.Authority;
import org.springframework.validation.annotation.Validated;

import java.util.Set;


@Validated
public record UserInputDto(

        @NotNull(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        String password,
        Role role,
        @JsonSerialize
        Set<Authority> authorities,
        Boolean enabled,
        String name,
        String address,
        String email

) {
}
