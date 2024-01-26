package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "customers")
public class Customer extends User {

    private int numberOfCats;

    @OneToMany(mappedBy = "customers")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany
    private Set<Cat> catListByName = new HashSet<>(); // set i.v.m. geen dubbelingen wenselijk

    public Customer() {}

    public Customer(int numberOfCats, Set<Cat> catListByName, List<Order> orderList) {
        this.numberOfCats = numberOfCats;
        this.orderList = orderList;
        this.catListByName = catListByName;
    }
}
