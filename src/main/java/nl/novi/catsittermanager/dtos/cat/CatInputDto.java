package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

    public record CatInputDto (

            Long id,  //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
            @NotNull
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
//    Customer ownerName
            String ownerName // Dummy, alleen voor los testen Cat class zonder database

    ) {}


//    @NotNull
//    public String name;
//    @Past
//    public LocalDate dateOfBirth; // omzetten naar DD-MM-JJJJ format
//    public String breed;
//    public String generalInfo;
//    public Boolean spayedOrNeutered; // true/false omzetten naar ja/nee
//    public Boolean vaccinated; // true/false omzetten naar ja/nee
//    public String veterinarianName;
//    public String  honeVet;
//    public String medicationName; // hoe kun je meer medicamenten opgeven?
//    public String medicationDose;
////    public Customer ownerName;
//    public String ownerName; // Alleen voor los testen Cat class zonder database
//}
