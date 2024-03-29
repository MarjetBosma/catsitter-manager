package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "catsitter")
    private List<Order> orders;

}
