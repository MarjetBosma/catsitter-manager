package nl.novi.catsittermanager.dtos.cat;

import net.datafaker.Faker;
import nl.novi.catsittermanager.dtos.order.OrderRequest;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class OrderRequestFactory {

    private static final Faker faker = new Faker();

    public static OrderRequest.OrderRequestBuilder randomOrderRequest() {

        return OrderRequest.builder()
                .startDate(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString())
                .endDate(faker.date().future(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString())
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .totalNumberOfVisits(faker.number().numberBetween(5, 20))
                .customerUsername(faker.name().username())
                .catsitterUsername(faker.name().username());
    }
}
