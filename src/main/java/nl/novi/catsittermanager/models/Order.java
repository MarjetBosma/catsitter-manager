package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID orderNo;

    private LocalDate startDate;

    private LocalDate endDate;

    private int dailyNumberOfVisits;

    private int totalNumberOfVisits;

    @OneToMany(mappedBy = "orders")
    private List<Task> tasks;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customers;

    @ManyToOne(fetch = FetchType.EAGER)
    private Catsitter catsitters;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "invoice_no")
    private Invoice invoice;

}
