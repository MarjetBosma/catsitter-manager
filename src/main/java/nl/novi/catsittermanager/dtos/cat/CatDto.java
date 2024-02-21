package nl.novi.catsittermanager.dtos.cat;

import nl.novi.catsittermanager.models.Customer;

import java.time.LocalDate;

public class CatDto {

    public Long id;

    public String name;
    public LocalDate dateOfBirth;
    public String breed;
    public String generalInfo;
    public String veterinarianName;
    public int phoneVet;
    public String medicationName;
    public String medicationDose;
    public Customer ownerName;

}
