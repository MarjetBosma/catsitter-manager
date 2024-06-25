package nl.novi.catsittermanager.dtos;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.helpers.OrderFactoryHelper;

import java.time.LocalDate;

public class OrderRequestFactory {

    private static final Faker faker = new Faker();

    public static OrderRequest.OrderRequestBuilder randomOrderRequest() {

        LocalDate startDate = OrderFactoryHelper.randomPastDate(30);
        LocalDate endDate = OrderFactoryHelper.randomFutureDate(30);

        return OrderRequest.builder()
                .startDate(String.valueOf(OrderFactoryHelper.randomPastDate(30)))
                .endDate(String.valueOf(OrderFactoryHelper.randomFutureDate(30)))
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .customerUsername(faker.name().username())
                .catsitterUsername(faker.name().username());
    }
}
