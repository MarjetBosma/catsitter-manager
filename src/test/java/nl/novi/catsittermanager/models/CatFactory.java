package nl.novi.catsittermanager.models;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CatFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static Cat.CatBuilder randomCat() {

        LocalDate startDate = LocalDate.of(2004, 1, 1);
        LocalDate endDate = LocalDate.now();

        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        return new Cat.CatBuilder()
                .id(UUID.fromString(faker.internet().uuid()))
                .name(faker.cat().name())
                .dateOfBirth(LocalDate.ofEpochDay(randomEpochDay))
                .gender(randomGender())//
                .breed(faker.cat().breed())
                .generalInfo(faker.lorem().paragraph())
                .spayedOrNeutered(faker.bool().bool())
                .vaccinated(faker.bool().bool())
                .veterinarianName(faker.name().fullName())
                .phoneVet(faker.phoneNumber().phoneNumber())
                .owner(CustomerFactory.randomCustomer());
                //.medicationName(randomMedicationName());
        //todo add medicine and owner
    }

    private static String randomGender() {
        Gender[] genders = Gender.values();
        Gender randomGender = genders[random.nextInt(genders.length)];
        return randomGender.toString();
    }

    private static MedicationName randomMedicationName() {
        MedicationName[] medications = MedicationName.values();
        return medications[random.nextInt(medications.length)];
    }
}