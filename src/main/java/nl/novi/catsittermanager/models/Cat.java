package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cats")

public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
    private Customer owner;

    private byte[] photo;

}
