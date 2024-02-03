package nl.novi.catsittermanager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CustomerCatSitterKey implements Serializable {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "catsitter_id")
    private Long catSitterId;

    public CustomerCatSitterKey() {}

    public CustomerCatSitterKey(Long customerId, Long catSitterId) {
        this.customerId = customerId;
        this.catSitterId = catSitterId;
    }

    public Long getCustomerId() { return customerId; }

    public Long getCatSitterId() { return catSitterId; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
