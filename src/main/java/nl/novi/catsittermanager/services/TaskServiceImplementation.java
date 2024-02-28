package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImplementation implements TaskService {

//    private final TaskRepository taskRepos;
//
//    private final OrderRepository orderRepos;
//
//    private final OrderServiceImplementation orderService;

    private List<Task> tasks = new ArrayList<>(); // voor testen zonder database

//    public TaskServiceImplementation(TaskRepository taskRepos, OrderRepository orderRepos, OrderServiceImplementation orderService) {
//        this.taskRepos = taskRepos;
//        this.orderRepos = orderRepos;
//        this.orderService = orderService;
//    }

    public TaskServiceImplementation() { // Bedoeld voor testen zonder database
        tasks.add(new Task(1L, TaskType.LITTERBOX, "Elke dag uitscheppen, schepje ligt bovenop de bak.", "Kattengrit is klontvormend, alleen klonten scheppen, niet in zijn geheel verschonen.", 5.00, "Hoort bij orderno 1" ));
        tasks.add(new Task(2L, TaskType.MEDICATION, "Eenmaal daags een pil verstoppen in het natvoer.", "Voorraad medicatie ligt in de kast waar ook het voer staat.", 7.00, "Hoort bij orderno 2"));
    }

    @Override
    public List<TaskDto> getAllTasks() {
//        List<Task> taskList = taskRepos.findAll(); // Deze is voor als de database gevuld is
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (Task task : tasks) {
            TaskDto taskDto = TaskMapper.transferToDto(task);
            taskDtoList.add(taskDto);
        }
        return taskDtoList;
    }

    @Override
    public TaskDto getTask(long idToFind) {
        for (Task task : tasks) {
            if (task.getTaskNo() == idToFind) {
                return TaskMapper.transferToDto(task);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id.");
    }

    @Override
    public TaskDto createTask(TaskInputDto taskInputDto) {
        Task newTask = TaskMapper.transferFromDto((taskInputDto));
        tasks.add(newTask);
        return TaskMapper.transferToDto(newTask);
    }

    @Override
    public TaskDto editTask(long idToEdit, TaskInputDto taskInputDto) {
        for (Task task : tasks) {
            if (task.getTaskNo() == idToEdit) {
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
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id.");
    }

    @Override
    public long deleteTask(long idToDelete) {
        for (Task task : tasks) {
            if (task.getTaskNo() == idToDelete) {
                tasks.remove(task);
                return idToDelete;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found with this id");
    }
}

// methodes schrijven om Task aan andere entiteiten te koppelen
