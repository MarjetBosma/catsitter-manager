package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepos;

    public TaskService(TaskRepository taskRepos) {
        this.taskRepos = taskRepos;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepos.findAll().stream()
                .map(TaskMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTask(UUID idToFind) {
        return taskRepos.findById(idToFind)
                .map(TaskMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    public TaskDto createTask(@RequestBody TaskInputDto taskInputDto) {
        Task newTask = TaskMapper.transferFromDto((taskInputDto));
        newTask.setOrder(taskInputDto.order());
        taskRepos.save(newTask);
        return TaskMapper.transferToDto(newTask);
    }

    public TaskDto editTask(UUID idToEdit, TaskInputDto taskInputDto) {
        Optional<Task> optionalTask = taskRepos.findById(idToEdit);

            if (optionalTask.isPresent()) {
                Task task  = optionalTask.get();
                if (taskInputDto.taskType() != null) {
                    task.setTaskType(taskInputDto.taskType());
                }
                if (task.getTaskInstruction() != null) {
                    task.setTaskInstruction(taskInputDto.taskInstruction());
                }
                if (taskInputDto.extraInstructions() != null) {
                    task.setExtraInstructions(taskInputDto.extraInstructions());
                }
                if (taskInputDto.priceOfTask() != 0) {
                    task.setPriceOfTask(taskInputDto.priceOfTask());
                }
                if (taskInputDto.order() != null) {
                    task.setOrder(taskInputDto.order());
                }
                return TaskMapper.transferToDto(task);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id.");
        }
    }

    public UUID deleteTask(UUID idToDelete) {
        taskRepos.deleteById(idToDelete);
        return idToDelete;
    }
}

