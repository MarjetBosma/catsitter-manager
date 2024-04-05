package nl.novi.catsittermanager.controllers;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")

public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> taskResponseList = taskService.getAllTasks().stream()
                .map(TaskMapper::TaskToTaskResponse)
                .toList();
        return ResponseEntity.ok(taskResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") final UUID idToFind) {
        Task task = taskService.getTask(idToFind);
        return ResponseEntity.ok(TaskMapper.TaskToTaskResponse(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody final TaskRequest taskRequest) {
        Task task = taskService.createTask(TaskMapper.TaskRequestToTask(taskRequest), taskRequest.orderNo());
        return ResponseEntity.ok().body(TaskMapper.TaskToTaskResponse(task));
    }

    // todo: Uitzoeken waarom deze een authentication error geeft (getest na gedeeltelijke implementatie security)
    // todo: beslissen of ik een versie met validation exception wil gebruiken
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("id") final UUID idToEdit, @RequestBody final TaskRequest taskRequest) {
        Task task = taskService.editTask(idToEdit, TaskMapper.TaskRequestToTask(taskRequest), taskRequest.orderNo());
        return ResponseEntity.ok().body(TaskMapper.TaskToTaskResponse(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") final UUID idToDelete) {
        taskService.deleteTask(idToDelete);
        return ResponseEntity.ok().body("Task with id " + idToDelete + " removed from database");
    }
}
