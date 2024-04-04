package nl.novi.catsittermanager.dtos.customer;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CustomerRequest(
        @NotNull
        String username,
        @NotNull
        String password,
        @NotNull
        String name,
        @NotNull
        String address,
        @NotNull
        String email

) {
}
