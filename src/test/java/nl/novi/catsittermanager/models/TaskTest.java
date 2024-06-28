package nl.novi.catsittermanager.models;

import nl.novi.catsittermanager.enumerations.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void testUpdatePriceOfTask_prePersist() {
        // Arrange
        task.setTaskType(TaskType.FOOD);

        // Act
        task.updatePriceOfTask();

        // Assert
        assertEquals(TaskType.FOOD.getPrice(), task.getPriceOfTask());
    }

    @Test
    void testUpdatePriceOfTask_preUpdate() {
        // Arrange
        task.setTaskType(TaskType.WATER);

        // Act
        task.updatePriceOfTask();

        // Assert
        assertEquals(TaskType.WATER.getPrice(), task.getPriceOfTask());
    }

    @Test
    void testConstructorAndGetters() {
        // Arrange
        UUID taskNo = UUID.randomUUID();
        TaskType taskType = TaskType.MEDICATION;
        String taskInstruction = "Give medication";
        String extraInstructions = "With food";
        double priceOfTask = taskType.getPrice();
        Order order = new Order();

        // Act
        Task task = new Task(taskNo, taskType, taskInstruction, extraInstructions, priceOfTask, order);

        // Assert
        assertEquals(taskNo, task.getTaskNo());
        assertEquals(taskType, task.getTaskType());
        assertEquals(taskInstruction, task.getTaskInstruction());
        assertEquals(extraInstructions, task.getExtraInstructions());
        assertEquals(priceOfTask, task.getPriceOfTask());
        assertEquals(order, task.getOrder());
    }

    @Test
    void testBuilder() {
        // Arrange
        UUID taskNo = UUID.randomUUID();
        TaskType taskType = TaskType.PLAY;
        String taskInstruction = "Play with the cat";
        String extraInstructions = "Use the toy mouse";
        double priceOfTask = taskType.getPrice();
        Order order = new Order();

        // Act
        Task task = Task.builder()
                .taskNo(taskNo)
                .taskType(taskType)
                .taskInstruction(taskInstruction)
                .extraInstructions(extraInstructions)
                .priceOfTask(priceOfTask)
                .order(order)
                .build();

        // Assert
        assertEquals(taskNo, task.getTaskNo());
        assertEquals(taskType, task.getTaskType());
        assertEquals(taskInstruction, task.getTaskInstruction());
        assertEquals(extraInstructions, task.getExtraInstructions());
        assertEquals(priceOfTask, task.getPriceOfTask());
        assertEquals(order, task.getOrder());
    }
}