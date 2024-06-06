package nl.novi.catsittermanager.models;

import net.datafaker.Faker;

import nl.novi.catsittermanager.enumerations.Role;

public class UserFactory {

    private static final Faker faker = new Faker();

    public static User.UserBuilder randomUser() {

        return User.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .email(faker.internet().emailAddress())
                .enabled(true)
                .role(Role.ADMIN);
    }
}
