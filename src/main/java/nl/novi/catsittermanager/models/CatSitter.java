package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//@Entity
//@Table(name = "catsitters")
public class CatSitter extends User {

    private Long id; //Dummy, alleen voor testen in Postman zonder gebruik van superclass (User levert normaalgesproken de id)

    private String about;

//    @OneToMany(mappedBy = "catsitters")
//    private List<Order> orderList = new ArrayList<>();

    private String orderList; // Dummy, alleen voor testen zonder database

//    @ManyToMany(mappedBy = "catsitters")
//    private List<Customer> customerList = new ArrayList<>();

    private String customerList; // Dummy, alleen voor testen zonder database

    public CatSitter(Long id, String about, String orderList, String customerList) { // Id is dummy, alleen voor separaat testen met Postman, komt normaalgesproken uit superklasse User
        this.id = id;
        this.about = about;
        this.orderList = orderList;
        this.customerList = customerList;
    }

//    public CatSitter(String about, List<Order> orderList, List<Customer> customers) {
//        super();
//        this.about = about;
//        this.orderList = orderList;
//        this.customerList = customers;
//    }
}
