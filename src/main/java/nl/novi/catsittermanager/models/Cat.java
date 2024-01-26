package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter

@Entity
@Table(name = "cats")

public class Cat {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private String breed;

    private String veterinarianName;

    private int phoneVet;

    private String medicationName;

    private String medicationDose;

    private String specialInstructions;

    // photo

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer ownerName;

    public Cat() {}

    public Cat(Long id, String name, LocalDate dateOfBirth, String breed, String veterinarianName, int phoneVet, String medicationName, String medicationDose, String specialInstructions, Customer ownerName) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.veterinarianName = veterinarianName;
        this.phoneVet = phoneVet;
        this.medicationName = medicationName;
        this.medicationDose = medicationDose;
        this.specialInstructions = specialInstructions;
        this.ownerName = ownerName;
    }

}
