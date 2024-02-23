package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter

//@Entity
//@Table(name = "orders")
public class Order {
//    @Id
//    @GeneratedValue
    private Long orderNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private int dailyNumberOfVisits;

    private int totalNumberOfVisits;

//    @OneToMany(mappedBy = "orders")
//    private List<Task> taskList;
    private String taskList; // Dummy, alleen voor los testen Order class zonder database

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Customer customer;
    private String customer; // Dummy, alleen voor los testen Order class zonder database

//    @ManyToOne(fetch = FetchType.EAGER)
//    private CatSitter catSitter;
    private String catSitter; // Dummy, alleen voor los testen Cat class zonder database

//    @OneToOne
//    private Invoice invoice;
    private String invoice; // Dummy, alleen voor los testen Cat class zonder database

    public Order() {
    }

    public Order(Long orderNo, LocalDate startDate, LocalDate endDate, int dailyNumberOfVisits, int totalNumberOfVisits, String taskList, String customer, String catSitter, String invoice) {
        this.orderNo = orderNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyNumberOfVisits = dailyNumberOfVisits;
        this.totalNumberOfVisits = totalNumberOfVisits;
        this.taskList = taskList; // List van maken
        this.customer = customer; // datatype bij database weer terugzetten naar Customer
        this.catSitter = catSitter; // datatype bij database weer terugzetten naar CatSitter
        this.invoice = invoice; // datatype bij database weer terugzetten naar Invoice
    }


}
