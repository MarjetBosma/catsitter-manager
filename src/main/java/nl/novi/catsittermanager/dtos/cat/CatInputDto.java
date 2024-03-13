package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Validated
    public record CatInputDto (

        UUID id,
        @NotNull (message = "cat name is required")
        String name,
        @Past
        LocalDate dateOfBirth,
        String breed,
        String generalInfo,
        Boolean spayedOrNeutered,
        Boolean vaccinated,
        String veterinarianName,
        String phoneVet,
        String medicationName,
        String medicationDose,
        @NotNull (message = "owner name is required")
        UUID ownerId

    ) {
    }

