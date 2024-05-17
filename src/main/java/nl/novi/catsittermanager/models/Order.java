package nl.novi.catsittermanager.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_no")
    private UUID orderNo;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "daily_number_of_visits")
    private int dailyNumberOfVisits;

    @Column(name = "total_number_of_visits")
    private int totalNumberOfVisits;

    @Column(name = "tasks")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @JoinColumn(name = "customer_username")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "catsitter_username")
    @ManyToOne(fetch = FetchType.LAZY)
    private Catsitter catsitter;

    @PrimaryKeyJoinColumn(name = "invoice")
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Invoice invoice;

}
