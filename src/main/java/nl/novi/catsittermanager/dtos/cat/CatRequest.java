package nl.novi.catsittermanager.dtos.cat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Builder
public record CatRequest(

        //Todo add null validation for needed fields
        @NotNull(message = "cat name is required")
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
        @NotNull(message = "owner name is required")
        String ownerUsername

) {
}

