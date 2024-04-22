package nl.novi.catsittermanager.models;

import net.datafaker.Faker;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OrderFactory {

    private static final Faker faker = new Faker();

    public static Order.OrderBuilder randomOrder() {

        return Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .endDate(faker.date().future(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .totalNumberOfVisits(faker.number().numberBetween(5, 20))
                .tasks(new ArrayList<>())
                .customer(CustomerFactory.randomCustomer().build())
                .catsitter(Catsitter.builder().name(faker.name().username()).build())
                .invoice(null);
    }

    public static List<Order> randomOrders(int count) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orders.add(randomOrder().build());
        }
        return orders;
    }
}
