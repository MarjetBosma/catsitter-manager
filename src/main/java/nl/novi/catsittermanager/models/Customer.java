package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "customers")
public class Customer extends User {

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> order;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Cat> cats;

    @ManyToMany
    @JoinTable(name = "customers_catsitters",
            joinColumns = @JoinColumn(name = "customers_id"),
            inverseJoinColumns = @JoinColumn(name = "catsitters_id"))
    private List<Catsitter> catsitter;

}


