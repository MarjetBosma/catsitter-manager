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
    private Long taskNo;
    private TaskType taskType;
    private String taskInstruction;
    private String extraInstructions;
    private double priceOfTask;

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Order order;

    private String order; // Dummy, alleen voor los testen Cat class zonder database

    public Task() {}

    public Task(Long taskNo, TaskType taskType, String taskInstruction, String extraInstructions, double priceOfTask, String order) {
        this.taskNo = taskNo;
        this.taskType = taskType;
        this.taskInstruction = taskInstruction;
        this.extraInstructions = extraInstructions;
        this.priceOfTask = priceOfTask;
        this.order = order; // datatype bij database weer terugzetten naar Order
    }
}
