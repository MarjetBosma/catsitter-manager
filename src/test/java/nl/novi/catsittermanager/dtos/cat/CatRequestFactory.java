package nl.novi.catsittermanager.dtos.cat;

import com.github.javafaker.Faker;

import static helpers.CatFactoryHelper.randomDateOfBirth;
import static helpers.CatFactoryHelper.randomGender;
import static helpers.CatFactoryHelper.randomMedicationDose;
import static helpers.CatFactoryHelper.randomMedicationName;

public class CatRequestFactory {

    private static final Faker faker = new Faker();

    public static CatRequest.CatRequestBuilder randomCatRequest() {
        return new CatRequest.CatRequestBuilder()
                .name(faker.cat().name())
                .dateOfBirth(randomDateOfBirth())
                .gender(randomGender())
                .breed(faker.cat().breed())
                .generalInfo(faker.lorem().paragraph())
                .spayedOrNeutered(faker.bool().bool())
                .vaccinated(faker.bool().bool())
                .veterinarianName(faker.name().fullName())
                .phoneVet(faker.phoneNumber().phoneNumber())
                .medicationName(randomMedicationName())
                .medicationDose(randomMedicationDose())
                .ownerUsername(faker.name().username());
    }
}