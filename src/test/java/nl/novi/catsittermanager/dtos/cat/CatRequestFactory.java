package nl.novi.catsittermanager.dtos.cat;

import com.github.javafaker.Faker;

import java.time.LocalDate;

public class CatRequestFactory {

    private static final Faker faker = new Faker();

    public static CatRequest.CatRequestBuilder randomCatRequest() {
        return new CatRequest.CatRequestBuilder()
                .name(faker.cat().name())
                .dateOfBirth(LocalDate.now()) //Todo get date stuff to a helper class
                .gender("male") ////Todo get gender stuff to a helper class
                .breed(faker.cat().breed())
                .generalInfo(faker.lorem().paragraph())
                .spayedOrNeutered(faker.bool().bool())
                .vaccinated(faker.bool().bool())
                .veterinarianName(faker.name().fullName())
                .phoneVet(faker.phoneNumber().phoneNumber())
                .ownerUsername(faker.name().username());
    }

}