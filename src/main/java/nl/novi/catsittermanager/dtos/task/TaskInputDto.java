package nl.novi.catsittermanager.dtos.task;

import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.Order;

public record TaskInputDto (
        Long taskNo, //Dummy, alleen voor testen in Postman, later id automatisch meegeven via database
        TaskType taskType,
        String taskInstruction,
        String extraInstructions,
        Double priceOfTask,

//    public Order order;
        String order // Alleen voor los testen Task class zonder database

) {}

