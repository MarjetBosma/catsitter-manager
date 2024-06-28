package nl.novi.catsittermanager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import nl.novi.catsittermanager.enumerations.TaskType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_no")
    private UUID taskNo;

    @Column(name = "task_type")
    private TaskType taskType;

    @Column(name = "task_instruction")
    private String taskInstruction;

    @Column(name = "extra_instructions")
    private String extraInstructions;

    @Column(name = "price_of_task")
    private double priceOfTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    @JsonBackReference
    private Order order;

    @PrePersist
    @PreUpdate
    void updatePriceOfTask() {
        if (this.taskType != null) {
            this.priceOfTask = this.taskType.getPrice();
        }
    }
}
