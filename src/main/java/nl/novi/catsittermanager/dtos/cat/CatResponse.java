package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.ImageUpload;

import java.time.LocalDate;
import java.util.UUID;

public record CatResponse(
        UUID id,
        String name,
        String dateOfBirth,
        String gender,
        String breed,
        String generalInfo,
        Boolean spayedOrNeutered,
        Boolean vaccinated,
        String veterinarianName,
        String phoneVet,
        String medicationName,
        String medicationDose,
        String ownerUsername,
        ImageUpload image

) {
}
