package nl.novi.catsittermanager.dtos.catsitter;

import jakarta.validation.constraints.NotNull;
import nl.novi.catsittermanager.models.FileUploadResponse;
import org.springframework.validation.annotation.Validated;


@Validated
public record CatsitterRequest(
        @NotNull
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
        FileUploadResponse image

) {
}

