package nl.novi.catsittermanager.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public int getDurationInDays() {
        return (int) (ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1);
    }

    public int calculateTotalNumberOfVisits() {
        return getDurationInDays() * this.dailyNumberOfVisits;
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (Task task : this.tasks) {
            totalCost += task.getTaskType().getPrice() * this.calculateTotalNumberOfVisits();
        }
        return totalCost;
    }
}
