package nl.novi.catsittermanager.dtos.task;

import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public record TaskInputDto (
        UUID taskNo,
        TaskType taskType,
        String taskInstruction,
        String extraInstructions,
        @Positive
        Double priceOfTask,
        Order order
) {
}

