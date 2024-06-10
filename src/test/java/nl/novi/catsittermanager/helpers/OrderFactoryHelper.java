package nl.novi.catsittermanager.helpers;

import net.datafaker.Faker;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class OrderFactoryHelper {

    private static final Faker faker = new Faker();

    public static LocalDate randomPastDate(int days) {
        return faker.date().past(days, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate randomFutureDate(int days) {
        return faker.date().future(days, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
