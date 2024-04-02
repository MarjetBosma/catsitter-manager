package nl.novi.catsittermanager.models;

import com.github.javafaker.Faker;
import java.util.Random;
import java.util.UUID;

import static helpers.CatFactoryHelper.randomDateOfBirth;
import static helpers.CatFactoryHelper.randomGender;
import static helpers.CatFactoryHelper.randomMedicationDose;
import static helpers.CatFactoryHelper.randomMedicationName;

public class CatFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static Cat.CatBuilder randomCat() {

        return new Cat.CatBuilder()
                .id(UUID.fromString(faker.internet().uuid()))
                .name(faker.cat().name())
                .dateOfBirth(randomDateOfBirth())
                .gender(randomGender())//
                .breed(faker.cat().breed())
                .generalInfo(faker.lorem().paragraph())
                .spayedOrNeutered(faker.bool().bool())
                .vaccinated(faker.bool().bool())
                .veterinarianName(faker.name().fullName())
                .phoneVet(faker.phoneNumber().phoneNumber())
                .medicationName(randomMedicationName())
                .medicationDose(randomMedicationDose())
                .owner(CustomerFactory.randomCustomer());
    }
}