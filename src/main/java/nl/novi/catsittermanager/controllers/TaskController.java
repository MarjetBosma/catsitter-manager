package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.IdInputDto;
import nl.novi.catsittermanager.dtos.task.TaskDto;
import nl.novi.catsittermanager.dtos.task.TaskInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.TaskServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") long idToFind) {
        if (idToFind > 0) {
            TaskDto taskDto = taskService.getTask(idToFind);
            return ResponseEntity.ok(taskDto);
        } else {
            throw new RecordNotFoundException("No task found with this id");
        }
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
    public ResponseEntity<TaskDto> editTask(@PathVariable("id") long id, @RequestBody TaskInputDto task) {
        TaskDto editedTask = taskService.editTask(id, task);

        return ResponseEntity.ok().body(editedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") Long idToDelete) {
        taskService.deleteTask(idToDelete);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{id}/order")
//    public ResponseEntity<Object> assignOrderToTask(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        taskService.assignOrderToTask(id, input.id);
//        return ResponseEntity.noContent().build();
//    }

}
