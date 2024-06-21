package nl.novi.catsittermanager.dtos.catsitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record CatsitterRequest(
        @NotNull(message = "Username is required")
        String username,
        @NotNull(message = "Password is required")
        String password,
        @NotNull(message = "Full name is required")
        String name,
        @NotNull(message = "Address is required")
        String address,
        @NotNull(message = "Email is required")
        String email,
        String about,
        @JsonIgnore
        ImageUpload image
) {
}

