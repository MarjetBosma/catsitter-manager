package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long orderNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private int dailyNumberOfVisits;

    private int totalNumberOfVisits;

    @OneToMany(mappedBy = "orders")
    private List<Task> taskList;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private CatSitter catSitter;

    @OneToOne
    private Invoice invoice;

    public Order() {
    }

    public Order(Long orderNo, LocalDate startDate, LocalDate endDate, int dailyNumberOfVisits, int totalNumberOfVisits, List<Task> taskList, Customer customer, CatSitter catSitter, Invoice invoice) {
        this.orderNo = orderNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyNumberOfVisits = dailyNumberOfVisits;
        this.totalNumberOfVisits = totalNumberOfVisits;
        this.taskList = taskList;
        this.customer = customer;
        this.catSitter = catSitter;
        this.invoice = invoice;
    }
}
