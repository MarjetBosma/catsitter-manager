package nl.novi.catsittermanager.services;

import org.springframework.stereotype.Service;

@Service
public class TaskServiceImplementation {



    private final OrderServiceImplementation orderService;


    public TaskServiceImplementation(OrderServiceImplementation orderService) {
        this.orderService = orderService;
    }
}
