package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @JoinColumn(name = "invoice_no")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID invoiceNo;

    private LocalDate invoiceDate;

    private Double amount;

    private Boolean paid;

    @OneToOne(mappedBy = "invoice")
    @PrimaryKeyJoinColumn
    private Order order;

}
