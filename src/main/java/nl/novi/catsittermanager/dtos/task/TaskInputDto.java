package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;

public class TaskInputDto {
    public TaskType taskType;
    public String taskInstruction;
    public String extraInstructions;
    public Double priceOfTask;

    public Order order;
}

