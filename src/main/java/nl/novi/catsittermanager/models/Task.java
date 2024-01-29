package nl.novi.catsittermanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String taskName;

    private String taskInstruction; // enum van maken?

    private double priceOfTask;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    public Task() {}

    public Task(Long id, String taskName, String taskInstruction, double priceOfTask, Order order) {
        this.id = id;
        this.taskName = taskName;
        this.taskInstruction = taskInstruction;
        this.priceOfTask = priceOfTask;
        this.order = order;
    }
}
