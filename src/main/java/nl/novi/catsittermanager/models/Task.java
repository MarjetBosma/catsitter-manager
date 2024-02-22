package nl.novi.catsittermanager.models;

import lombok.Getter;
import lombok.Setter;
import nl.novi.catsittermanager.enumerations.TaskType;

@Getter
@Setter

//@Entity
//@Table(name = "tasks")
public class Task {

//    @Id
//    @GeneratedValue
    private Long id;
    private TaskType taskType;
    private String taskInstruction;
    private double priceOfTask;
    private String extraInstructions;

//    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    public Task() {}

    public Task(Long id, TaskType taskType, String taskInstruction, String extraInstructions, double priceOfTask, Order order) {
        this.id = id;
        this.taskType = taskType;
        this.taskInstruction = taskInstruction;
        this.extraInstructions = extraInstructions;
        this.priceOfTask = priceOfTask;
        this.order = order;
    }
}
