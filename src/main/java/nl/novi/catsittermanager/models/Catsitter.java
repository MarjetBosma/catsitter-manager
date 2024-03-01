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

//    @OneToMany(mappedBy = "catsitters")
//    private List<Order> orderList = new ArrayList<>();

    private String orderList; // Dummy, alleen voor testen zonder database

//    @ManyToMany(mappedBy = "catsitters")
//    private List<Customer> customerList = new ArrayList<>();

    private String customerList; // Dummy, alleen voor testen zonder database

    public Catsitter() {}

    public Catsitter(String about, String orderList, String customerList) {
        super();
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
