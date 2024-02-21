package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImplementation {

    private final TaskRepository taskRepos;

    private final OrderRepository orderRepos;

    private final OrderServiceImplementation orderService;


    public TaskServiceImplementation(TaskRepository taskRepos, OrderRepository orderRepos, OrderServiceImplementation orderService) {
        this.taskRepos = taskRepos;
        this.orderRepos = orderRepos;
        this.orderService = orderService;
    }
}
