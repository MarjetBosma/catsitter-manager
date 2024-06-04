package nl.novi.catsittermanager.dtos.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import nl.novi.catsittermanager.enumerations.TaskType;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Builder
@Validated
public record TaskRequest(
        @NotNull
        TaskType taskType,
        @NotNull
        String taskInstruction,
        String extraInstructions,
        @NotNull
        @Positive
        Double priceOfTask,
        UUID orderNo

) {
}

