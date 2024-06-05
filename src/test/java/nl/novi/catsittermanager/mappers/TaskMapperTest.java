package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.TaskRequestFactory;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.models.TaskFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskMapperTest {

    @Test
    void testTaskToTaskResponse() {

        // Given
        Task task = TaskFactory.randomTask().build();

        // When
        TaskResponse taskResponse = TaskMapper.TaskToTaskResponse(task);

        // Then
        assertEquals(task.getTaskNo(), taskResponse.taskNo());
        assertEquals(task.getTaskType(), taskResponse.taskType());
        assertEquals(task.getTaskInstruction(), taskResponse.taskInstruction());
        assertEquals(task.getExtraInstructions(), taskResponse.extraInstructions());
        assertEquals(task.getPriceOfTask(), taskResponse.priceOfTask());
        assertEquals(task.getOrder().getOrderNo(), taskResponse.orderNo());
    }

    @Test
    void testTaskRequestToTask() {

        // Given
        TaskRequest taskRequest = TaskRequestFactory.randomTaskRequest().build();

        // When
        Task task = TaskMapper.TaskRequestToTask(taskRequest);

        // Then
        assertEquals(taskRequest.taskType(), task.getTaskType());
        assertEquals(taskRequest.taskInstruction(), task.getTaskInstruction());
        assertEquals(taskRequest.extraInstructions(), task.getExtraInstructions());
        assertEquals(taskRequest.priceOfTask(), task.getPriceOfTask());
    }
}
