package nl.novi.catsittermanager.dtos.task;

import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

@Validated
public record TaskInputDto (
        Long taskNo, //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
        TaskType taskType,
        String taskInstruction,
        String extraInstructions,
        @Positive
        Double priceOfTask,
        Order order
) {}

