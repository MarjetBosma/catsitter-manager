package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class CatInputDto {

    @NotNull
    public String name;
    @Past
    public LocalDate dateOfBirth;
    public String breed;
    public String generalInfo;
    public String veterinarianName;
    public int phoneVet; // moet 10 cijfers zijn
    public String medicationName;
    public String medicationDose;
    public Customer ownerName;
}
