package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepos;

    private final OrderRepository orderRepos;

    private final OrderService orderService;


    public TaskService(TaskRepository taskRepos, OrderRepository orderRepos, OrderService orderService) {
        this.taskRepos = taskRepos;
        this.orderRepos = orderRepos;
        this.orderService = orderService;
    }

    public TaskDto transferToDto(Task task) {

        TaskDto taskDto = new TaskDto();

        taskDto.id = task.getId();
        taskDto.taskType = task.getTaskType();
        taskDto.taskInstruction = task.getTaskInstruction();
        taskDto.extraInstructions = task.getExtraInstructions();
        taskDto.priceOfTask = task.getPriceOfTask();
        taskDto.order = task.getOrder();

        return taskDto;
    }

    public Task transferToTask(TaskInputDto taskDto) {

        Task task = new Task();

        task.setTaskType(taskDto.taskType);
        task.setTaskInstruction(taskDto.taskInstruction);
        task.setExtraInstructions(taskDto.extraInstructions);
        task.setPriceOfTask(taskDto.priceOfTask);
        task.setOrder(taskDto.order);

        return task;
    }
}
