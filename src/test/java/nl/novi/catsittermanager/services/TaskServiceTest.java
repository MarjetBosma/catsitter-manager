package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.models.TaskFactory;
import nl.novi.catsittermanager.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.plaf.BorderUIResource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetAllTasks() {
        // Given
        Task expectedTask = TaskFactory.randomTask().build();
        List<Task> expectedTaskList = List.of(expectedTask);

        when(taskRepository.findAll()).thenReturn(expectedTaskList);

        // When
        List<Task> actualTaskList = taskService.getAllTasks();

        // Then
        assertEquals(expectedTaskList, actualTaskList);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasks_noTasksInDatabase_shouldReturnEmptyList() {
        // Given
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Task> taskResponseList = taskService.getAllTasks();

        // Then
        assertTrue(taskResponseList.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreateTask_shouldCreateANewTask() {
        // Given
        Task expectedTask = TaskFactory.randomTask().build();
        Order order = OrderFactory.randomOrder().build();

        when(orderService.getOrder(order.getOrderNo())).thenReturn(order);
        when(taskRepository.save(expectedTask)).thenReturn(expectedTask);

        // When
        Task resultTask = taskService.createTask(expectedTask, order.getOrderNo());

        // Then
        assertEquals(expectedTask, resultTask);

        verify(orderService, times(1)).getOrder(order.getOrderNo());
        verify(taskRepository, times(1)).save(expectedTask);
    }

    @Test
    void testEditTask_shouldEditExistingTask() {
        // Given
        Task task = TaskFactory.randomTask().build();

        when(taskRepository.findById(task.getTaskNo())).thenReturn(Optional.of(task));
        when(orderService.getOrder(task.getOrder().getOrderNo())).thenReturn(task.getOrder());
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task resultTask = taskService.editTask(task.getTaskNo(), task, task.getOrder().getOrderNo());

        // Then
        assertEquals(task, resultTask);

        verify(taskRepository, times(1)).findById(task.getTaskNo());
        verify(orderService, times(1)).getOrder(task.getOrder().getOrderNo());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void testEditTask_nonExistingTask_shouldThrowRecordNotFoundException() {
        // Given
        UUID taskNo = UUID.randomUUID();
        when(taskRepository.findById(taskNo)).thenReturn(Optional.empty());

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> taskService.getTask(taskNo));

        // Then
        assertEquals("No task found with this id.", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskNo);
    }

    @Test
    void testDeleteTask_shouldDeleteTaskWithSpecificId() {
        // Given
        Task task = TaskFactory.randomTask().build();
        when(taskRepository.existsById(task.getTaskNo())).thenReturn(true);

        // When
        UUID taskNo = taskService.deleteTask(task.getTaskNo());

        // Then
        verify(taskRepository, times(1)).existsById(taskNo);
        verify(taskRepository, times(1)).deleteById(taskNo);
        verifyNoInteractions(orderService);
    }

    @Test
    void testDeleteTask_shouldDeleteTaskWithSpecificId_RecordNotFoundException() {
        // Given
        UUID taskNo = UUID.randomUUID();
        when(taskRepository.existsById(taskNo)).thenReturn(false);

        // When
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> taskService.deleteTask(taskNo));

        // Them
        assertEquals("No task found with this id.", exception.getMessage());
        verify(taskRepository, times(1)).existsById(taskNo);
        verify(taskRepository, never()).deleteById(taskNo);
        verifyNoInteractions(orderService);
    }
}


