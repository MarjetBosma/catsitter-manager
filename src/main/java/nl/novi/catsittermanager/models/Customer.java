package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

//@Entity
//@Table(name = "customers")
public class Customer extends User {

    private int numberOfCats;

//    @OneToMany(mappedBy = "customers")
    private List<Order> orderList = new ArrayList<>();

//    @OneToMany(mappedBy = "customers")
    private Set<Cat> catListByName = new HashSet<>(); // set i.v.m. geen dubbelingen wenselijk

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "customers_catsitters", joinColumns = @JoinColumn(name = "customers_id"), inverseJoinColumns = @JoinColumn(name = "catsitters_id"))

    private List<CatSitter> catSitters = new ArrayList<>();

    public Customer() {}

    public Customer(int numberOfCats, Set<Cat> catListByName, List<Order> orderList, List<CatSitter> catSitters) {
        this.numberOfCats = numberOfCats;
        this.orderList = orderList;
        this.catListByName = catListByName;
        this.catSitters = catSitters;
    }
}
