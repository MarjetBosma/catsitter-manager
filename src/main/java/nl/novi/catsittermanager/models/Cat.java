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
import jakarta.persistence.PrimaryKeyJoinColumn;
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
