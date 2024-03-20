package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.models.Task;

public class TaskMapper {
    public static TaskDto transferToDto(Task task) {
        return new TaskDto(task.getTaskNo(),
                task.getTaskType(),
                task.getTaskInstruction(),
                task.getExtraInstructions(),
                task.getPriceOfTask(),
                task.getOrder()
        );
    }

    public static Task transferFromDto(TaskInputDto taskInputDto) {
        return Task.builder().taskType(taskInputDto.taskType())
                .taskInstruction(taskInputDto.taskInstruction())
                .extraInstructions(taskInputDto.extraInstructions())
                .priceOfTask(taskInputDto.priceOfTask())
                .order(taskInputDto.order())
                .build();
    }
}
