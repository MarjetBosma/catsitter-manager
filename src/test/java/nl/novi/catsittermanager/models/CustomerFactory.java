package nl.novi.catsittermanager.models;

import net.datafaker.Faker;

import nl.novi.catsittermanager.enumerations.Role;

import java.util.Collections;


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
                .role(Role.CUSTOMER)
                .orders(Collections.emptyList())
                .cats(Collections.emptyList());
    }
}
