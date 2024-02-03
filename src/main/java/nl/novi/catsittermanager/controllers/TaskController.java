package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.IdInputDto;
import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/tasks")

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        if (id > 0) {
            TaskDto taskDto = TaskService.getTaskId();
            return ResponseEntity.ok(taskDto);
        } else {
            throw new RecordNotFoundException("No task found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskInputDto taskInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            TaskDto savedTask;
            savedTask = taskService.createTask(taskInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedTask.id).toUriString());
            return ResponseEntity.created(uri).body(savedTask);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable long id, @RequestBody TaskInputDto task) {
        TaskDto changeTaskId = taskService.updateTask(id, task);

        return ResponseEntity.ok().body(changeTaskId);
    }

    @PutMapping("/{id}/order")
    public ResponseEntity<Object> assignOrderToTask(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        taskService.assignOrderToTask(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
