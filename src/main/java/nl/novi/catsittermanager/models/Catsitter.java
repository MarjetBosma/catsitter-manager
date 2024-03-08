package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    private Long id; // Moet eigenlijk worden overgenomen van User, uitzoeken hoe dit werkt

    private String about;

    @OneToMany(mappedBy = "catsitters")
    private Order order;

//    @ManyToMany(mappedBy = "customers")
    private Customer customer;

    public Catsitter() {}

    public Catsitter(String about, Order order, Customer customer) {
        super();
        this.about = about;
        this.order = order;
        this.customer = customer;
    }
}
