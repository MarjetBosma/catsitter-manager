package nl.novi.catsittermanager.models;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter

//@Entity
//@Table(name = "invoices")
public class Invoice {

//    @Id
//    @GeneratedValue
    private Long invoiceNo;

    @FutureOrPresent
    private LocalDate invoiceDate;

    private Double amount;

    private Boolean paid;

//    @OneToOne
//    private Order order;

    private String order; // Dummy, alleen voor los testen Invoice class zonder database

    public Invoice() {}

    public Invoice(Long invoiceNo, LocalDate invoiceDate, Double amount, Boolean paid, String order) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.paid = paid;
        this.order = order; // object type bij database weer terugzetten naar Customer
    }
}
