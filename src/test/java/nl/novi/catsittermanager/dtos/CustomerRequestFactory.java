package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;

public class CustomerRequestFactory {

    private static final Faker faker = new Faker();

    public static CustomerRequest.CustomerRequestBuilder randomCustomerRequest() {

        return CustomerRequest.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .email(faker.internet().emailAddress());
    }

}
