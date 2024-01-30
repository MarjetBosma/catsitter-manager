package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.models.Order;

public class TaskDto {
    public Long id;
    public String taskName;
    public String taskInstruction; // enum van maken?
    public Double priceOfTask;

    public Order order;
}
