package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepos;

//    private final OrderRepository orderRepos;
//
//    private final OrderServiceImplementation orderService;

    public TaskServiceImplementation(TaskRepository taskRepos
//    , OrderRepository orderRepos, OrderServiceImplementation orderService
    ) {
        this.taskRepos = taskRepos;
//        this.orderRepos = orderRepos;
//        this.orderService = orderService;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<TaskDto> taskDtoList = new ArrayList<>();
        List<Task> taskList = taskRepos.findAll();

        for (Task task : taskList) {
            TaskDto taskDto = TaskMapper.transferToDto(task);
            taskDtoList.add(taskDto);
        }
        return taskDtoList;
    }

    @Override
    public TaskDto getTask(long idToFind) {
        Optional<Task> taskOptional = taskRepos.findById(idToFind);
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
        taskRepos.save(newTask);
        return TaskMapper.transferToDto(newTask);
    }

    @Override
    public TaskDto editTask(long idToEdit, TaskInputDto taskInputDto) {
        Optional<Task> optionalTask = taskRepos.findById(idToEdit);

            if (optionalTask.isPresent()) {
                Task task  = optionalTask.get();
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
    public String deleteTask(long idToDelete) {
        Optional<Task> optionalTask = taskRepos.findById(idToDelete);
        if (optionalTask.isPresent()) {
            taskRepos.deleteById(idToDelete);
            return "Task with id " + idToDelete +  " removed from database";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id");
        }
    }
}


// methodes schrijven om Task aan andere entiteiten te koppelen
