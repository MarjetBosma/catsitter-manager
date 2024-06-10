package nl.novi.catsittermanager.helpers;

import nl.novi.catsittermanager.enums.Gender;
import nl.novi.catsittermanager.enums.MedicationName;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CatFactoryHelper {

    private static final Random random = new Random();

    public static LocalDate randomDateOfBirth() {
        LocalDate startDate = LocalDate.of(2004, 1, 1);
        LocalDate endDate = LocalDate.now();

        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static String randomGender() {
        Gender[] genders = Gender.values();
        Gender randomGender = genders[random.nextInt(genders.length)];
        return randomGender.toString();
    }

    public static String randomMedicationName() {
        MedicationName[] medications = MedicationName.values();
        return String.valueOf(medications[random.nextInt(medications.length)]);
    }

    public static String randomMedicationDose() {
        MedicationName[] medications = MedicationName.values();
        return String.valueOf(medications[random.nextInt(medications.length)]);
    }
}
