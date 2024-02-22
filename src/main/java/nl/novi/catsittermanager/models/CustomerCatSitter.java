package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//@Entity
public class CustomerCatSitter {
//    @EmbeddedId
    private CustomerCatSitterKey id;

//    @ManyToOne
//    @MapsId("customerId")
//    @JoinColumn(name = "customer_id")
    private Customer customer;

//    @ManyToOne
//    @MapsId("catsitterId")
//    @JoinColumn(name = "catsitter_id")
    private CatSitter catSitter;
    public CustomerCatSitterKey getId() {
        return id;
    }
}
