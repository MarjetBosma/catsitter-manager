package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "CustomerBuilder")
@Entity
@Table(name = "customers")
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Order> order;

    @OneToMany(mappedBy = "owner")
    private List<Cat> cat;

    @ManyToMany
    @JoinTable(name = "customers_catsitters",
            joinColumns = @JoinColumn(name = "customers_id"),
            inverseJoinColumns = @JoinColumn(name = "catsitters_id"))
    private List<Catsitter> catsitter;
}


