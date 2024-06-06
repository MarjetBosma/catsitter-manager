package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.user.UserRequest;

public class UserRequestFactory {

    private static final Faker faker = new Faker();

    public static UserRequest.UserRequestBuilder randomUserRequest() {

        return UserRequest.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .email(faker.internet().emailAddress());
    }

}


