package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;

import jakarta.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

@Validated
public record TaskInputDto (
        TaskType taskType,
        String taskInstruction,
        String extraInstructions,
        @Positive
        Double priceOfTask,
        Order order

) {
}

