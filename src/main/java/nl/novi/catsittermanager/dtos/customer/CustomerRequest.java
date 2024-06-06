package nl.novi.catsittermanager.dtos.customer;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record CustomerRequest(
        @NotNull(message = "Username is required")
        String username,
        @NotNull(message = "Password is required")
        String password,
        @NotNull(message = "Full name is required")
        String name,
        @NotNull(message = "Address is required")
        String address,
        @NotNull(message = "Email address is required")
        String email
) {
}
