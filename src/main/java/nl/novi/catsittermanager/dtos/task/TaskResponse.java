package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;

import java.util.UUID;

public record TaskResponse(

        UUID taskNo,
        TaskType taskType,
        String taskInstruction,
        String extraInstructions,
        Double priceOfTask,
        UUID orderNo

) {
}
