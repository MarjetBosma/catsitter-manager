package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;

import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks();

    TaskDto getTask(long idToFind);

    TaskDto createTask(TaskInputDto taskInputDto);

    TaskDto editTask(long idToEdit, TaskInputDto taskInputDto);

    long deleteTask(long idToDelete);
}

