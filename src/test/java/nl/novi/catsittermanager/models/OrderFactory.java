package nl.novi.catsittermanager.models;

import net.datafaker.Faker;
import nl.novi.catsittermanager.helpers.OrderFactoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderFactory {

    private static final Faker faker = new Faker();

    public static Order.OrderBuilder randomOrder(List<Task> tasks) {

        return Order.builder()
                .orderNo(UUID.randomUUID())
                .startDate(OrderFactoryHelper.randomPastDate(30))
                .endDate(OrderFactoryHelper.randomFutureDate(30))
                .dailyNumberOfVisits(faker.number().numberBetween(1, 5))
                .totalNumberOfVisits(faker.number().numberBetween(5, 20))
                .tasks(tasks)
                .customer(CustomerFactory.randomCustomer().build())
                .catsitter(CatsitterFactory.randomCatsitter().build())
                .invoice(null);
    }

    public static List<Order> randomOrders(int count, List<Task> tasks) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orders.add(randomOrder(tasks).build());
        }
        return orders;
    }
}