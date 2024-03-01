package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "customers")
public class Customer extends User {

    private Long id; // Moet eigenlijk worden overgenomen van User, uitzoeken hoe dit werkt

    private int numberOfCats;

//    @OneToMany(mappedBy = "customers")
//    private List<Order> orderList = new ArrayList<>();

    private String orderList; // Dummy, alleen voor testen zonder database

//    @OneToMany(mappedBy = "customers")
//    private List<Cat> catListByName = new ArrayList<>();

    private String catListByName; // Dummy, alleen voor testen zonder database

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "customers_catsitters", joinColumns = @JoinColumn(name = "customers_id"), inverseJoinColumns = @JoinColumn(name = "catsitters_id"))

//    private List<CatSitter> catSitters = new ArrayList<>();

    private String catsitterList; // Dummy, alleen voor testen zonder database

    public Customer() {}

    public Customer(int numberOfCats, String catListByName, String orderList, String catsitterList) { // Dummy, alleen voor testen in Postman zonder database
        super();
        this.numberOfCats = numberOfCats;
        this.catListByName = catListByName;
        this.orderList = orderList;
        this.catsitterList = catsitterList;
    }

//    public Customer(int numberOfCats, List<Cat> catListByName, List<Order> orderList, List<CatSitter> catsitterList) {
//        super();
//        this.numberOfCats = numberOfCats;
//        this.orderList = orderList;
//        this.catListByName = catListByName;
//        this.catsitterList = catSitterList;
//    }


}


