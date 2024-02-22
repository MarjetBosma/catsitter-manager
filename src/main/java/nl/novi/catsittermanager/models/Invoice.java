package nl.novi.catsittermanager.models;

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

    private LocalDate invoiceDate;

    private Double amount;

//    @OneToOne
    private Order order;

    public Invoice() {}

    public Invoice(Long invoiceNo, LocalDate invoiceDate, Double amount, Order order) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.order = order;
    }
}
