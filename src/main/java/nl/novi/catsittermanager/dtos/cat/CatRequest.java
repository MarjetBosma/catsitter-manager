package nl.novi.catsittermanager.dtos.cat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Builder
public record CatRequest(

        @NotNull(message = "cat name is required")
        String name,
        @NotNull
        @Past
        LocalDate dateOfBirth,
        @NotNull
        String gender,
        @NotNull
        String breed,
        String generalInfo,
        @NotNull
        Boolean spayedOrNeutered,
        @NotNull
        Boolean vaccinated,
        @NotNull
        String veterinarianName,
        @NotNull
        String phoneVet,
        String medicationName,
        String medicationDose,
        @NotNull(message = "owner name is required")
        String ownerUsername

) {
}

