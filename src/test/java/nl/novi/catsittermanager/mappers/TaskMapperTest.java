package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.TaskRequestFactory;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.models.TaskFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskMapperTest {

    @Test
    void testTaskToTaskResponse() {

        // Arrange
        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        Order order = OrderFactory.randomOrder(tasks).build();
        Task task = TaskFactory.randomTask().order(order).build();

        // Act
        TaskResponse taskResponse = TaskMapper.TaskToTaskResponse(task);

        // Assert
        assertEquals(task.getTaskNo(), taskResponse.taskNo());
        assertEquals(task.getTaskType(), taskResponse.taskType());
        assertEquals(task.getTaskInstruction(), taskResponse.taskInstruction());
        assertEquals(task.getExtraInstructions(), taskResponse.extraInstructions());
        assertEquals(task.getPriceOfTask(), taskResponse.priceOfTask());
        assertEquals(task.getOrder().getOrderNo(), taskResponse.orderNo());
    }

    @Test
    void testTaskToTaskResponseWithNullOrder() {
        // Arrange
        Task task = Task.builder().taskNo(UUID.randomUUID()).taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build();

        // Act & Assert
        AssertionError exception = assertThrows(AssertionError.class, () -> TaskMapper.TaskToTaskResponse(task));
    }

    @Test
    void testTaskRequestToTask() {

        // Arrange
        TaskRequest taskRequest = TaskRequestFactory.randomTaskRequest().build();

        Order order = Order.builder().orderNo(taskRequest.orderNo()).build();

        // Act
        Task task = TaskMapper.TaskRequestToTask(taskRequest, order);
        task.setOrder(order);

        // Assert
        assertEquals(taskRequest.taskType(), task.getTaskType());
        assertEquals(taskRequest.taskInstruction(), task.getTaskInstruction());
        assertEquals(taskRequest.extraInstructions(), task.getExtraInstructions());

        double expectedPriceOfTask = taskRequest.taskType().getPrice();
        assertEquals(expectedPriceOfTask, task.getPriceOfTask());

        assertEquals(taskRequest.taskType(), task.getTaskType());
        assertEquals(taskRequest.taskInstruction(), task.getTaskInstruction());
        assertEquals(taskRequest.extraInstructions(), task.getExtraInstructions());
        assertEquals(taskRequest.orderNo(), task.getOrder().getOrderNo());
    }

    @Test
    void testTaskRequestToTaskWithNullOrder() {
        // Arrange
        TaskRequest taskRequest = TaskRequest.builder()
                .taskType(TaskType.FOOD)
                .taskInstruction("Feed the cat")
                .extraInstructions("Use the blue bowl")
                .orderNo(UUID.randomUUID())
                .build();

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> TaskMapper.TaskRequestToTask(taskRequest, null));
    }
}
