package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "CatsitterBuilder")
@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String about;

    @OneToMany(mappedBy = "catsitter")
    private List<Order> order;

    @ManyToMany(mappedBy = "catsitter")
    private List<Customer> customer;

}
