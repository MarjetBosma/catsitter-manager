package nl.novi.catsittermanager.models;

import java.io.Serializable;

//@Embeddable
public class CustomerCatsitterKey implements Serializable {

//    @Column(name = "customer_id")
    private Long customerId;

//    @Column(name = "catsitter_id")
    private Long catSitterId;

    public CustomerCatsitterKey() {}

    public CustomerCatsitterKey(Long customerId, Long catSitterId) {
        this.customerId = customerId;
        this.catSitterId = catSitterId;
    }

    public Long getCustomerId() { return customerId; }

    public Long getCatSitterId() { return catSitterId; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
