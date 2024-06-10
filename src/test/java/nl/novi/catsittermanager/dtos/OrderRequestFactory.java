package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.helpers.OrderFactoryHelper;

public class OrderRequestFactory {

    private static final Faker faker = new Faker();

    public static OrderRequest.OrderRequestBuilder randomOrderRequest() {

        return OrderRequest.builder()
                .startDate(String.valueOf(OrderFactoryHelper.randomPastDate(30)))
                .endDate(String.valueOf(OrderFactoryHelper.randomFutureDate(30)))
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .totalNumberOfVisits(faker.number().numberBetween(5, 20))
                .customerUsername(faker.name().username())
                .catsitterUsername(faker.name().username());
    }
}
