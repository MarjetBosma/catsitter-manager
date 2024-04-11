package nl.novi.catsittermanager.models;

import com.github.javafaker.Faker;
import nl.novi.catsittermanager.helpers.CatFactoryHelper;

import java.util.UUID;

public class CatFactory {

    private static final Faker faker = new Faker();

    public static Cat.CatBuilder randomCat() {

        return new Cat.CatBuilder()
                .id(UUID.fromString(faker.internet().uuid()))
                .name(faker.cat().name())
                .dateOfBirth(CatFactoryHelper.randomDateOfBirth())
                .gender(CatFactoryHelper.randomGender())//
                .breed(faker.cat().breed())
                .generalInfo(faker.lorem().paragraph())
                .spayedOrNeutered(faker.bool().bool())
                .vaccinated(faker.bool().bool())
                .veterinarianName(faker.name().fullName())
                .phoneVet(faker.phoneNumber().phoneNumber())
                .medicationName(CatFactoryHelper.randomMedicationName())
                .medicationDose(CatFactoryHelper.randomMedicationDose())
                .owner(CustomerFactory.randomCustomer());
    }
}