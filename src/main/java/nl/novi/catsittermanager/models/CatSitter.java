package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

//@Entity
//@Table(name = "catsitters")
public class CatSitter extends User {

    private String about;

//    @OneToMany(mappedBy = "catsitters")
    private List<Order> orderList = new ArrayList<>();

//    @ManyToMany(mappedBy = "catsitters")
    private List<Customer> customers = new ArrayList<>();

    public CatSitter() {}

    public CatSitter(String about, List<Order> orderList, List<Customer> customers) {
        this.about = about;
        this.orderList = orderList;
        this.customers = customers;
    }
}
