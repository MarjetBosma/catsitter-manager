package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "catsitters")
public class Catsitter extends User {

    private String about;

//    @OneToMany(mappedBy = "catsitters")
//    private List<Order> orderList = new ArrayList<>();

    private String orderList; // Dummy, alleen voor testen zonder database

//    @ManyToMany(mappedBy = "catsitters")
//    private List<Customer> customerList = new ArrayList<>();

    private String customerList; // Dummy, alleen voor testen zonder database

    public Catsitter() {}

    public Catsitter(String about, String orderList, String customerList) { // Id is dummy, alleen voor separaat testen met Postman, komt normaalgesproken uit superklasse User
        this.about = about;
        this.orderList = orderList;
        this.customerList = customerList;
    }

//    public Catsitter(String about, List<Order> orderList, List<Customer> customers) {
//        super();
//        this.about = about;
//        this.orderList = orderList;
//        this.customerList = customers;
//    }

}
