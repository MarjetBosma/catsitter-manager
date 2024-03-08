package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private int dailyNumberOfVisits;

    private int totalNumberOfVisits;

    @OneToMany(mappedBy = "orders")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Catsitter catsitter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "invoice_no")
    private Invoice invoice;

    public Order() {}

    public Order(Long orderNo, LocalDate startDate, LocalDate endDate, int dailyNumberOfVisits, int totalNumberOfVisits, Task task, Customer customer, Catsitter catsitter, Invoice invoice) {
        this.orderNo = orderNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyNumberOfVisits = dailyNumberOfVisits;
        this.totalNumberOfVisits = totalNumberOfVisits;
        this.task = task;
        this.customer = customer;
        this.catsitter = catsitter;
        this.invoice = invoice;
    }
}
