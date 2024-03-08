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
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImplementation(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(final UUID idToFind) {
        Optional<Task> taskOptional = taskRepository.findById(idToFind);
        if (taskOptional.isPresent()) {
            return TaskMapper.transferToDto(taskOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id.");
        }
    }

    @Override
    public TaskDto createTask(@RequestBody TaskInputDto taskInputDto) {
        Task newTask = new Task();
        newTask.setTaskNo(taskInputDto.taskNo());
        newTask.setTaskType(taskInputDto.taskType());
        newTask.setTaskInstruction(taskInputDto.taskInstruction());
        newTask.setExtraInstructions(taskInputDto.extraInstructions());
        newTask.setOrder(taskInputDto.order());
        taskRepository.save(newTask);
        return TaskMapper.transferToDto(newTask);
    }

    @Override
    public TaskDto editTask(UUID idToEdit, TaskInputDto taskInputDto) {
        Optional<Task> optionalTask = taskRepository.findById(idToEdit);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (taskInputDto.taskType() != null) {
                task.setTaskType(taskInputDto.taskType());
            }
            if (task.getTaskInstruction() != null) {
                task.setTaskInstruction(taskInputDto.taskInstruction());
            }
            if (taskInputDto.extraInstructions() != null) { // Mag eigenlijk wel null zijn, is optioneel
                task.setExtraInstructions(taskInputDto.extraInstructions());
            }
            if (taskInputDto.priceOfTask() != 0) {
                task.setPriceOfTask(taskInputDto.priceOfTask());
            }
            return TaskMapper.transferToDto(task);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id.");
        }
    }

    @Override
    public UUID deleteTask(UUID idToDelete) {
        Optional<Task> optionalTask = taskRepository.findById(idToDelete);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(idToDelete);
            return idToDelete;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id");
        }
    }
}
