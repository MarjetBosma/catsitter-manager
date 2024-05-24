package nl.novi.catsittermanager.dtos.cat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record CatRequest(

        @NotNull(message = "cat name is required")
        String name,
        @NotNull(message = "date of birth is required")
        String dateOfBirth,
        @NotNull(message = "gender is required")
        String gender,
        @NotNull(message = "cat breed is required")
        String breed,
        String generalInfo,
        @NotNull(message = "required to enter whether the  cat is spayed or neutered")
        Boolean spayedOrNeutered,
        @NotNull(message = "vaccination status is required")
        Boolean vaccinated,
        @NotNull(message = "name of veterinarian or vet clinic name is required")
        String veterinarianName,
        @NotNull(message = "phone number vet clinic is required")
        String phoneVet,
        String medicationName,
        String medicationDose,
        @NotNull(message = "owner name is required")
        String ownerUsername,
        ImageUpload image
) {
}

