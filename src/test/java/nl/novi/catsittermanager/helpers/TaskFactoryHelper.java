package nl.novi.catsittermanager.helpers;

import net.datafaker.Faker;
import nl.novi.catsittermanager.enumerations.TaskType;

public class TaskFactoryHelper {

    private static final Faker faker = new Faker();

    public static TaskType randomTaskType() {
        return TaskType.values()[faker.number().numberBetween(0, TaskType.values().length)];
    }
}
