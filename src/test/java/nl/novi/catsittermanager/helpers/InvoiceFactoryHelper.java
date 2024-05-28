package nl.novi.catsittermanager.helpers;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class InvoiceFactoryHelper {

    private static final Faker faker = new Faker();

    public static LocalDate randomDateIn2024() {
        Date startDate = Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2024, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date randomDate = faker.date().between(startDate, endDate);
        return randomDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
