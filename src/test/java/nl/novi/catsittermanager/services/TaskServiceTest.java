package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.enumerations.TaskType;
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

        // Arrange
        Task expectedTask = TaskFactory.randomTask().build();
        List<Task> expectedTaskList = List.of(expectedTask);

        when(taskRepository.findAll()).thenReturn(expectedTaskList);

        // Act
        List<Task> actualTaskList = taskService.getAllTasks();

        // Assert
        assertEquals(expectedTaskList, actualTaskList);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasks_noTasksInDatabase_shouldReturnEmptyList() {

        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Task> taskResponseList = taskService.getAllTasks();

        // Assert
        assertTrue(taskResponseList.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTask_shouldFetchTaskWithSpecificId() {

        // Arrange
        Task expectedTask = TaskFactory.randomTask().build();
        when(taskRepository.findById(expectedTask.getTaskNo())).thenReturn(Optional.of(expectedTask));

        // Act
        Task resultTask = taskService.getTask(expectedTask.getTaskNo());

        // Assert
        assertEquals(expectedTask, resultTask);
        verify(taskRepository, times(1)).findById(expectedTask.getTaskNo());
    }

    @Test
    void testGetTask_shouldFetchTaskWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> taskService.getTask(taskId));

        // Assert
        assertEquals("No task found with this id.", exception.getMessage());
        verify(taskRepository).findById(taskId);
        verifyNoMoreInteractions(taskRepository);
    }


    @Test
    void testCreateTask_shouldCreateANewTask() {

        // Arrange
        Task expectedTask = TaskFactory.randomTask().build();

        List<Task> existingTasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );
        Order expectedOrder = OrderFactory.randomOrder(existingTasks).build();

        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);
        when(taskRepository.save(expectedTask)).thenReturn(expectedTask);

        // Act
        Task resultTask = taskService.createTask(expectedTask, expectedOrder.getOrderNo());

        // Assert
        assertEquals(expectedTask, resultTask);
        assertEquals(expectedOrder, resultTask.getOrder());

        verify(orderService, times(1)).getOrder(expectedOrder.getOrderNo());
        verify(taskRepository, times(1)).save(expectedTask);
    }

    @Test
    void testEditTask_withOrder_shouldEditTaskWithOrderPresent() {
        // Arrange
        Task task = TaskFactory.randomTask().build();

        List<Task> existingTasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order expectedOrder = OrderFactory.randomOrder(existingTasks).build();
        task.setOrder(expectedOrder);

        when(taskRepository.findById(task.getTaskNo())).thenReturn(Optional.of(task));
        when(orderService.getOrder(expectedOrder.getOrderNo())).thenReturn(expectedOrder);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task resultTask = taskService.editTask(task.getTaskNo(), task, expectedOrder.getOrderNo());

        // Assert
        assertEquals(task, resultTask);
        assertEquals(expectedOrder, resultTask.getOrder());

        verify(taskRepository, times(1)).findById(task.getTaskNo());
        verify(orderService, times(1)).getOrder(expectedOrder.getOrderNo());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testEditTask_nonExistingTask_shouldThrowRecordNotFoundException() {

        // Arrange
        UUID taskNo = UUID.randomUUID();
        Task task = TaskFactory.randomTask().build();
        when(taskRepository.findById(taskNo)).thenReturn(Optional.empty());
        UUID orderNo = UUID.randomUUID();

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> taskService.editTask(taskNo, task, orderNo));

        // Assert
        assertEquals("No task found with this id.", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskNo);
    }


    @Test
    void testDeleteTask_shouldDeleteTaskWithSpecificId() {

        // Arrange
        Task task = TaskFactory.randomTask().build();
        when(taskRepository.existsById(task.getTaskNo())).thenReturn(true);

        // Act
        UUID taskNo = taskService.deleteTask(task.getTaskNo());

        // Assert
        verify(taskRepository, times(1)).existsById(taskNo);
        verify(taskRepository, times(1)).deleteById(taskNo);
        verifyNoInteractions(orderService);
    }

    @Test
    void testDeleteTask_shouldDeleteTaskWithSpecificId_RecordNotFoundException() {

        // Arrange
        UUID taskNo = UUID.randomUUID();
        when(taskRepository.existsById(taskNo)).thenReturn(false);

        // Act
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> taskService.deleteTask(taskNo));

        // Assert
        assertEquals("No task found with this id.", exception.getMessage());

        verify(taskRepository, times(1)).existsById(taskNo);
        verify(taskRepository, never()).deleteById(taskNo);
        verifyNoInteractions(orderService);
    }
}


