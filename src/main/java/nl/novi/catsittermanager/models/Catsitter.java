package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "catsitters")
public class Catsitter extends User {

    private UUID id; // Moet eigenlijk worden overgenomen van User, uitzoeken hoe dit werkt

    private String about;

    @OneToMany(mappedBy = "catsitter")
    private List<Order> order;


    @ManyToMany(mappedBy = "catsitter")
    private List<Customer> customer;


}
