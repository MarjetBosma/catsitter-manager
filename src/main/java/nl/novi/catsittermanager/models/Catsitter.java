package nl.novi.catsittermanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "CatsitterBuilder")
@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    private String about;

    @JsonIgnore
    @OneToMany(mappedBy = "catsitter", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(mappedBy = "catsitter", cascade = CascadeType.ALL)
    ImageUpload image;

}
