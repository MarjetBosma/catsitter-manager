package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder()
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @JoinColumn(name = "invoice_no")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID invoiceNo;

    @FutureOrPresent
    private LocalDate invoiceDate;

    @Positive
    private Double amount;

    private Boolean paid;

    @OneToOne(mappedBy = "invoice")
    @PrimaryKeyJoinColumn
    private Order order;

}
