package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "CatsitterBuilder")
@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    private String about;

    @OneToMany(mappedBy = "catsitters")
    private List<Order> orders;

//    @ManyToMany(mappedBy = "catsitters")
//    private List<Customer> customers;

}
