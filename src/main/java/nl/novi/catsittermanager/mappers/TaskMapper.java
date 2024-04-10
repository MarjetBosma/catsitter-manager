package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.models.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public static TaskResponse TaskToTaskResponse(Task task) {
        return new TaskResponse(
                task.getTaskNo(),
                task.getTaskType(),
                task.getTaskInstruction(),
                task.getExtraInstructions(),
                task.getPriceOfTask(),
                task.getOrder().getOrderNo()
        );
    }

    public static Task TaskRequestToTask(TaskRequest taskRequest) {
        return Task.builder()
                .taskType(taskRequest.taskType())
                .taskInstruction(taskRequest.taskInstruction())
                .extraInstructions(taskRequest.extraInstructions())
                .priceOfTask(taskRequest.priceOfTask())
                .build();
    }
}
