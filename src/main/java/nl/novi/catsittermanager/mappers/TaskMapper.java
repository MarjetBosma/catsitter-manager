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
        return new Task(taskInputDto.taskNo(), // In een latere fase deze hier niet meegeven, maar automatisch via database
                        taskInputDto.taskType(),
                        taskInputDto.taskInstruction(),
                        taskInputDto.extraInstructions(),
                        taskInputDto.priceOfTask(),
                        taskInputDto.order()
        );
    }
}
