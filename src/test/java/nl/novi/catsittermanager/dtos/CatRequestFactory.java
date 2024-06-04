package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.cat.CatRequest;

import static nl.novi.catsittermanager.helpers.CatFactoryHelper.*;

public class CatRequestFactory {

    private static final Faker faker = new Faker();

    public static CatRequest.CatRequestBuilder randomCatRequest() {

        return CatRequest.builder()
                .name(faker.cat().name())
                .dateOfBirth(randomDateOfBirth().toString())
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