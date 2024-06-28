package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.task.TaskRequest;
import nl.novi.catsittermanager.helpers.TaskFactoryHelper;

import java.util.UUID;

public class TaskRequestFactory {

    private static final Faker faker = new Faker();

    public static TaskRequest.TaskRequestBuilder randomTaskRequest() {

        return TaskRequest.builder()
                .taskType(TaskFactoryHelper.randomTaskType())
                .taskInstruction(faker.lorem().sentence())
                .extraInstructions(faker.lorem().sentence())
                .orderNo(UUID.randomUUID());
    }
}
