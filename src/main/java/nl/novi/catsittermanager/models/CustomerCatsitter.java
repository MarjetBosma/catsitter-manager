package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//@Entity
public class CustomerCatsitter {
//    @EmbeddedId
    private CustomerCatsitterKey id;

//    @ManyToOne
//    @MapsId("customerId")
//    @JoinColumn(name = "customer_id")
    private Customer customer;

//    @ManyToOne
//    @MapsId("catsitterId")
//    @JoinColumn(name = "catsitter_id")
    private Catsitter catSitter;
    public CustomerCatsitterKey getId() {
        return id;
    }
}
