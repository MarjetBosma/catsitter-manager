package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.TaskServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/task")

public class TaskController {

    private final TaskServiceImplementation taskService;

    public TaskController(TaskServiceImplementation taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") UUID idToFind) {

        TaskDto taskDto = taskService.getTask(idToFind);
        return ResponseEntity.ok(taskDto);

//            throw new RecordNotFoundException("No task found with this id");

    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskInputDto taskInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            TaskDto savedTask = taskService.createTask(taskInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedTask).toUriString());
            return ResponseEntity.created(uri).body(savedTask);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> editTask(@PathVariable("id") UUID id, @RequestBody TaskInputDto task) {
        TaskDto editedTask = taskService.editTask(id, task);

        return ResponseEntity.ok().body(editedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") UUID idToDelete) {
        taskService.deleteTask(idToDelete);
        return ResponseEntity.ok().body("Task with id " + idToDelete + " removed from database");
    }
}
