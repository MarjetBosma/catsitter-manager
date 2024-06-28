package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "invoice_no")
    private UUID invoiceNo;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "paid")
    private Boolean paid;

    @JoinColumn(name = "order_no")
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @PrePersist
    @PreUpdate
    void calculateAmount() {
        if (this.order != null) {
            this.amount = this.order.calculateTotalCost();
        }
    }

}
