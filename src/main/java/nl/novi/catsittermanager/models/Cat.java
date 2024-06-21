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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "breed")
    private String breed;

    @Column(name = "general_info")
    private String generalInfo;

    @Column(name = "spayed_or_neutered")
    private Boolean spayedOrNeutered;

    @Column(name = "vaccinated")
    private Boolean vaccinated;

    @Column(name = "veterinarian_name")
    private String veterinarianName;

    @Column(name = "phone_vet")
    private String phoneVet;

    @Column(name = "medication_name")
    private String medicationName;

    @Column(name = "medication_dose")
    private String medicationDose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_username")
    private Customer owner;

    @OneToOne(mappedBy = "cat", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "image")
    ImageUpload image;

}
