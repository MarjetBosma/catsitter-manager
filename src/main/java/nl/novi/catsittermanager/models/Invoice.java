package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @JoinColumn(name = "invoice_no")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long invoiceNo;

    @FutureOrPresent
    private LocalDate invoiceDate;

    @Positive
    private Double amount;

    private Boolean paid;

    @OneToOne(mappedBy = "invoice")
    @PrimaryKeyJoinColumn
    private Order order;

    public Invoice() {}

    public Invoice(Long invoiceNo, LocalDate invoiceDate, Double amount, Boolean paid, Order order) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.paid = paid;
        this.order = order;
    }
}
