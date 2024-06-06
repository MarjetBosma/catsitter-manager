package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final OrderService orderService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(final UUID idToFind) {
        return taskRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No task found with this id."));
    }

    public Task createTask(final Task task, UUID orderNo) {
        Order order = orderService.getOrder(orderNo);
        task.setOrder(order);
        return taskRepository.save(task);
    }

    public Task editTask(final UUID idToEdit, final Task task, final UUID orderNo) {
        if (taskRepository.findById(idToEdit).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No task found with this id.");
        }
        Order order = orderService.getOrder(orderNo);
        task.setOrder(order);
        return taskRepository.save(task);
    }

    public UUID deleteTask(UUID idToDelete) {
        if (!taskRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No task found with this id.");
        }
        taskRepository.deleteById(idToDelete);
        return idToDelete;
    }
}

