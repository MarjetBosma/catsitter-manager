package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import java.time.LocalDate;
import java.util.UUID;

public record CatDto (
    UUID id,
    String name,
    LocalDate dateOfBirth, // omzetten naar DD-MM-JJJJ format?
    String breed,
    String generalInfo,
    Boolean spayedOrNeutered, // true/false omzetten naar ja/nee
    Boolean vaccinated, // true/false omzetten naar ja/nee
    String veterinarianName,
    String phoneVet,
    String medicationName,
    String medicationDose,
    String ownerName

) {
}
