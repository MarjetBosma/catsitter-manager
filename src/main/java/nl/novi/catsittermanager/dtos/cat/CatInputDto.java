package nl.novi.catsittermanager.dtos.cat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
    public record CatInputDto (

        @NotNull (message = "cat name is required")
        String name,
        @Past
        LocalDate dateOfBirth,
        String gender,
        String breed,
        String generalInfo,
        Boolean spayedOrNeutered,
        Boolean vaccinated,
        String veterinarianName,
        String phoneVet,
        String medicationName,
        String medicationDose,
        @NotNull (message = "owner name is required")
        String ownerUsername

    ) {
    }

