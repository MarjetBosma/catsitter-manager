package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "customers")
public class Customer extends User {

    private Long id;

    private int numberOfCats;

    @OneToMany(mappedBy = "customers")
    private Order order;

    @OneToMany(mappedBy = "customers")
    private Cat cat;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "customers_catsitters", joinColumns = @JoinColumn(name = "customers_id"), inverseJoinColumns = @JoinColumn(name = "catsitters_id"))
    private Catsitter catsitter;

    public Customer() {}

    public Customer(int numberOfCats, Cat cat, Order order, Catsitter catsitter) {
        super();
        this.numberOfCats = numberOfCats;
        this.cat = cat;
        this.order = order;
        this.catsitter = catsitter;
    }
}


