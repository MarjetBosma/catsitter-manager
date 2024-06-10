package nl.novi.catsittermanager.models;

import net.datafaker.Faker;
import nl.novi.catsittermanager.helpers.TaskFactoryHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.models.OrderFactory.randomOrder;

public class TaskFactory {

    private static final Faker faker = new Faker();

    public static Task.TaskBuilder randomTask() {

        return Task.builder()
                .taskNo(UUID.randomUUID())
                .taskType(TaskFactoryHelper.randomTaskType())
                .taskInstruction(faker.lorem().sentence())
                .extraInstructions(faker.lorem().sentence())
                .priceOfTask(faker.number().randomDouble(2, 10, 100))
                .order(randomOrder().build());
    }

    public static List<Task> randomTasks(int count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(randomTask().build());
        }
        return tasks;
    }
}
