package nl.novi.catsittermanager.models;

import com.github.javafaker.Faker;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactory {

    private static final Faker faker = new Faker();

    public static Customer randomCustomer() {

        return Customer.CustomerBuilder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .build();

        //Todo all all fields
    }

}