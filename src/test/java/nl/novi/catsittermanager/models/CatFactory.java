package nl.novi.catsittermanager.models;

import net.datafaker.Faker;
import nl.novi.catsittermanager.helpers.CatFactoryHelper;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Cat> randomCats(int count) {
        List<Cat> cats = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cats.add(randomCat().build());
        }
        return cats;
    }
}