package nl.novi.catsittermanager.models;

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
    private UUID taskNo;
    private TaskType taskType;
    private String taskInstruction;
    private String extraInstructions;
    private double priceOfTask;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"order\"")
    private Order order;

}
