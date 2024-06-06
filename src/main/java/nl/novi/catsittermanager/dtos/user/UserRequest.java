package nl.novi.catsittermanager.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.novi.catsittermanager.enumerations.Role;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record UserRequest(
        @NotNull(message = "Username is required")
        String username,
        @NotNull(message = "Password is required")
        String password,
        Role role,
        Boolean enabled,
        @NotNull(message = "Full name is required")
        String name,
        @NotNull(message = "Address is required")
        String address,
        @NotNull(message = "Email address is required")
        String email

) {
}
