package nl.novi.catsittermanager.models;

import net.datafaker.Faker;

import java.util.Collections;

import static nl.novi.catsittermanager.enumerations.Role.CUSTOMER;

public class CustomerFactory {

    private static final Faker faker = new Faker();

    public static Customer.CustomerBuilder randomCustomer() {

        return Customer.CustomerBuilder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .email(faker.internet().emailAddress())
                .enabled(true)
                .orders(Collections.emptyList())
                .cats(Collections.emptyList());
    }
}
