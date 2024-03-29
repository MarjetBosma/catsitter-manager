package nl.novi.catsittermanager.models;

import com.github.javafaker.Faker;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFactory {

    private static final Faker faker = new Faker();

    public static Customer anCustomer() {

        return Customer.CustomerBuilder()
                .name(faker.name().fullName())
                .username(faker.name().username())
                .build();

        //Todo all all fields
    }

}