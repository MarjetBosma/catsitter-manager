package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskDto> getAllTasks();

    TaskDto getTask(UUID idToFind);

    TaskDto createTask(TaskInputDto taskInputDto);

    TaskDto editTask(UUID idToEdit, TaskInputDto taskInputDto);

    UUID deleteTask(UUID idToDelete);
}

