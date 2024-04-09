package nl.novi.catsittermanager.dtos.cat;

import java.time.LocalDate;
import java.util.UUID;

public record CatResponse(
        UUID id,
        String name,
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
        String ownerUsername

) {
}