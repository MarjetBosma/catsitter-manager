package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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

    private String gender;

    private String breed;

    private String generalInfo;

    private Boolean spayedOrNeutered;

    private Boolean vaccinated;

    private String veterinarianName;

    private String phoneVet;

    private String medicationName;

    private String medicationDose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Customer owner;

    private byte[] photo;

}
