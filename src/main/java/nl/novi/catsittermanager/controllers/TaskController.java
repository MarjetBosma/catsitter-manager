package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.services.OrderService;
import nl.novi.catsittermanager.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class TaskController {

    private final TaskService taskService;

    private final OrderService orderService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> taskResponseList = taskService.getAllTasks().stream()
                .map(TaskMapper::TaskToTaskResponse)
                .toList();
        return ResponseEntity.ok(taskResponseList);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") final UUID idToFind) {
        Task task = taskService.getTask(idToFind);
        return ResponseEntity.ok(TaskMapper.TaskToTaskResponse(task));
    }

    @PostMapping("/task")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody final TaskRequest taskRequest) throws URISyntaxException {
        UUID orderNo = taskRequest.orderNo();
        Order order = orderService.getOrder(orderNo);
        Task task = taskService.createTask(
                TaskMapper.TaskRequestToTask(taskRequest, order),
                taskRequest.orderNo()
        );
        Task savedTask = taskService.createTask(task, orderNo);

        return ResponseEntity.created(new URI("/task/" + task.getTaskNo()))
                .body(TaskMapper.TaskToTaskResponse(savedTask));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("id") final UUID idToEdit, @Valid @RequestBody final TaskRequest taskRequest) {
        UUID orderNo = taskRequest.orderNo();
        Order order = orderService.getOrder(orderNo);

        if (order == null) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id");
        }

        Task updatedTask = TaskMapper.TaskRequestToTask(taskRequest, order);
        updatedTask.setTaskNo(idToEdit);
        Task savedTask = taskService.editTask(updatedTask);

        return ResponseEntity.ok().body(TaskMapper.TaskToTaskResponse(savedTask));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") final UUID idToDelete) {
        taskService.deleteTask(idToDelete);
        return ResponseEntity.ok().body("Task with id " + idToDelete + " removed from database.");
    }
}
