package nl.novi.catsittermanager.models;

import net.datafaker.Faker;
import nl.novi.catsittermanager.helpers.OrderFactoryHelper;

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
                .startDate(OrderFactoryHelper.randomPastDate(30))
                .endDate(OrderFactoryHelper.randomFutureDate(30))
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .totalNumberOfVisits(faker.number().numberBetween(5, 20))
                .tasks(new ArrayList<>())
                .customer(CustomerFactory.randomCustomer().build())
                .catsitter(CatsitterFactory.randomCatsitter().build())
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
