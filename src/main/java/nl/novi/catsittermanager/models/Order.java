package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long orderNo;

    private int amountOfVisits;

    private List<Task> taskList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private CatSitter catSitter;

    @OneToOne
    private Invoice invoice;

    @OneToMany(mappedBy = "orders")
    private List<Order> orderList = new ArrayList<>();

    public Order() {}

    public Order(Long orderNo, int amountOfVisits, List<Task> taskList, Customer customer, CatSitter catSitter, Invoice invoice) {
        this.orderNo = orderNo;
        this.amountOfVisits = amountOfVisits;
        this.taskList = taskList;
        this.customer = customer;
        this.catSitter = catSitter;
        this.invoice = invoice;
    }
}
