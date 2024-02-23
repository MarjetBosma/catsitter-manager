package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import java.time.LocalDate;

public record CatDto (
    Long id,
    String name,
    LocalDate dateOfBirth,
    String breed,
    String generalInfo,
    Boolean spayedOrNeutered,
    Boolean vaccinated,
    String veterinarianName,
    String phoneVet,
    String medicationName,
    String medicationDose,

//    Customer ownerName
    String ownerName // Alleen voor los testen Cat class zonder database

) {}
