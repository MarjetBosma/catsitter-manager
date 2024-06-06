package nl.novi.catsittermanager.dtos.cat;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record CatRequest(
        @NotNull(message = "Cat name is required")
        String name,
        @NotNull(message = "Date of birth is required")
        String dateOfBirth,
        @NotNull(message = "Dender is required")
        String gender,
        @NotNull(message = "Cat breed is required")
        String breed,
        String generalInfo,
        @NotNull(message = "Required to enter whether the cat is spayed or neutered")
        Boolean spayedOrNeutered,
        @NotNull(message = "Vaccination status is required")
        Boolean vaccinated,
        @NotNull(message = "Name of veterinarian or vet clinic name is required")
        String veterinarianName,
        @NotNull(message = "Phone number vet clinic is required")
        String phoneVet,
        String medicationName,
        String medicationDose,
        @NotNull(message = "Owner name is required")
        String ownerUsername,
        ImageUpload image
) {
}

