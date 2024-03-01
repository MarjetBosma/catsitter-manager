package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
    @Validated
    public record CatInputDto (

            Long id,  //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
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
            //    @NotNull
            //    Customer ownerName
            @NotNull (message = "owner name is required")
            String ownerName  // Dummy, alleen voor los testen Cat class zonder database

    ) {}

