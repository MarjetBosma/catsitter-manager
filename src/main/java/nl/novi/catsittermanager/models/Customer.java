package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "CustomerBuilder")
@Entity
@Table(name = "customers")
public class Customer extends User {

    @OneToMany(mappedBy = "customers")
    private List<Order> orders;

    @OneToMany(mappedBy = "owner")
    private List<Cat> cats;

//    @ManyToMany
//    @JoinTable(name = "customers_catsitters",
//            joinColumns = @JoinColumn(name = "customer_id"),
//            inverseJoinColumns = @JoinColumn(name = "catsitter_id"))
//    private List<Catsitter> catsitters;

}


