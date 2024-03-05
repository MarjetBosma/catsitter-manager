package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;

public record TaskDto (
    Long taskNo,
    TaskType taskType,
    String taskInstruction,
    String extraInstructions,
    Double priceOfTask,
    Order order

) {}
