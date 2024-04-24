package nl.novi.catsittermanager.dtos.catsitter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.validation.annotation.Validated;

@Validated
public record CatsitterRequest(
        @NotBlank(message = "username is required")
        String username,
        @NotNull
        String password,
        @NotNull
        String name,
        @NotNull
        String address,
        @NotNull
        String email,
        String about,
        ImageUpload image

) {
}

