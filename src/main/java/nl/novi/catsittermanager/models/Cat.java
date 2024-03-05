package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "cats")

public class Cat {

    @Id
    @GeneratedValue
    private UUID id; // UUID i.p.v. long op advies van Berend, nog niet bij alle klassen toegepast

    private String name;

    private LocalDate dateOfBirth;

    private String breed;

    private String generalInfo;

    private Boolean spayedOrNeutered;

    private Boolean vaccinated;

    private String veterinarianName;

    private String phoneVet;

    private String medicationName;

    private String medicationDose;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer ownerName;

    // photo

    public Cat() {}

    public Cat(UUID id, String name, LocalDate dateOfBirth, String breed, String generalInfo, Boolean spayedOrNeutered, Boolean vaccinated, String veterinarianName, String phoneVet, String medicationName, String medicationDose, Customer ownerName) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.generalInfo = generalInfo;
        this.spayedOrNeutered = spayedOrNeutered;
        this.vaccinated = vaccinated;
        this.veterinarianName = veterinarianName;
        this.phoneVet = phoneVet;
        this.medicationName = medicationName;
        this.medicationDose = medicationDose;
        this.ownerName = ownerName;
    }
}
