package nl.novi.catsittermanager.dtos.customer;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CustomerRequest(
        @NotNull(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        String password,
        @NotNull(message = "name is required")
        String name,
        @NotNull(message = "address is required")
        String address,
        @NotNull(message = "email is required")
        String email

) {
}
