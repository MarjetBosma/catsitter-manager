package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;

public class CatsitterRequestFactory {

    private static final Faker faker = new Faker();

    public static CatsitterRequest.CatsitterRequestBuilder randomCatsitterRequest() {

        return CatsitterRequest.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .email(faker.internet().emailAddress())
                .about(faker.lorem().paragraph());
    }

}
