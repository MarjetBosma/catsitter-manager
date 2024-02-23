package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

//@Entity
//@Table(name = "cats")

public class Cat {

//    @Id
//    @GeneratedValue
    private Long id;

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

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Customer ownerName;

    private String ownerName; // Dummy, alleen voor los testen Cat class zonder database

    // photo

    public Cat() {}

    public Cat(Long id, String name, LocalDate dateOfBirth, String breed, String generalInfo, Boolean spayedOrNeutered, Boolean vaccinated, String veterinarianName, String phoneVet, String medicationName, String medicationDose, String ownerName) {
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
        this.ownerName = ownerName; // datatype bij database weer terugzetten naar Customer
    }
}
