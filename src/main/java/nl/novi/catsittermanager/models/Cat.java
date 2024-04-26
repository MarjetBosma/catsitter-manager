package nl.novi.catsittermanager.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn
    private Customer owner;

    @OneToOne(mappedBy = "cat", cascade = CascadeType.ALL)
    ImageUpload image;

}
