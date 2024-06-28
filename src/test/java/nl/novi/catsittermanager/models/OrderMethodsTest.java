package nl.novi.catsittermanager.models;

import nl.novi.catsittermanager.enumerations.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMethodsTest {

    private Order order;

    @BeforeEach
    void setUp() {
        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );

        order = Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .dailyNumberOfVisits(2)
                .tasks(tasks)
                .build();
    }

    @AfterEach
    void tearDown() {
        order = null;
    }

    @Test
    void test_durationInDays() {
        int expectedDuration = 6; // Including the start day
        int actualDuration = order.getDurationInDays();
        assertEquals(expectedDuration, actualDuration, "The duration in days should be correct.");
    }

    @Test
    void test_calculateTotalNumberOfVisits() {
        int expectedTotalNumberOfVisits = 12; // 6 days * 2 visits per day
        int actualTotalNumberOfVisits = order.calculateTotalNumberOfVisits();
        assertEquals(expectedTotalNumberOfVisits, actualTotalNumberOfVisits, "The total number of visits should be correct.");
    }

    @Test
    void test_calculateTotalCost() {
        double expectedTotalCost = 12 * (TaskType.FOOD.getPrice() + TaskType.WATER.getPrice()); // 12 visits * sum of task prices
        double actualTotalCost = order.calculateTotalCost();
        assertEquals(expectedTotalCost, actualTotalCost, "The total cost should be correct.");
    }
}