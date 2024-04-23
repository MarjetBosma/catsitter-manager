package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.helpers.ControllerHelper.checkForBindingResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class TaskController {

    private final TaskService taskService;

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
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody final TaskRequest taskRequest) {
        Task task = taskService.createTask(TaskMapper.TaskRequestToTask(taskRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.TaskToTaskResponse(task));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("id") final UUID idToEdit, @RequestBody final TaskRequest taskRequest) {
        Task task = taskService.editTask(idToEdit, TaskMapper.TaskRequestToTask(taskRequest), taskRequest.orderNo());
        return ResponseEntity.ok().body(TaskMapper.TaskToTaskResponse(task));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") final UUID idToDelete) {
        taskService.deleteTask(idToDelete);
        return ResponseEntity.ok().body("Task with id " + idToDelete + " removed from database");
    }
}
