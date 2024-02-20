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
}
