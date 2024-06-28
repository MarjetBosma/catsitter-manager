package nl.novi.catsittermanager.dtos.task;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.novi.catsittermanager.enumerations.TaskType;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Builder
@Validated
public record TaskRequest(
        @NotNull(message = "Task type is required, choose from food, water, litterbox, medication, furcare, play and other")
        TaskType taskType,
        @NotNull(message = "Providing a basic instruction for the task is required")
        String taskInstruction,
        String extraInstructions,
        @NotNull(message = "Give the number of the order to which this task belongs")
        UUID orderNo

) {
}

