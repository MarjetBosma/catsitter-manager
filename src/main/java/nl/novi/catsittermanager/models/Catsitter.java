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
@Builder
@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String about;

    @OneToMany(mappedBy = "catsitters")
    private List<Order> order;

    @ManyToMany(mappedBy = "customers")
    private List<Customer> customer;

}
