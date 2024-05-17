package nl.novi.catsittermanager.dtos.catsitter;

import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.validation.annotation.Validated;

@Validated
public record CatsitterRequest(
        @NotNull(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        String password,
        @NotNull(message = "name is required")
        String name,
        @NotNull(message = "address is required")
        String address,
        @NotNull(message = "email is required")
        String email,
        String about,
        ImageUpload image

) {
}

