package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.OrderRequestFactory;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.enumerations.TaskType;
import nl.novi.catsittermanager.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    @Test
    void testOrderToOrderResponse() {

        // Arrange
        Customer customer = new Customer();
        customer.setUsername("customerUsername");

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername("catsitterUsername");

        List<Task> tasks = List.of(
                Task.builder().taskType(TaskType.FOOD).priceOfTask(TaskType.FOOD.getPrice()).build(),
                Task.builder().taskType(TaskType.WATER).priceOfTask(TaskType.WATER.getPrice()).build()
        );
        Order order = OrderFactory.randomOrder(tasks).build();
        order.setCustomer(customer);
        order.setCatsitter(catsitter);

        // Act
        OrderResponse orderResponse = OrderMapper.OrderToOrderResponse(order);

        // Assert
        assertEquals(order.getOrderNo(), orderResponse.orderNo());
        assertEquals(order.getStartDate().toString(), orderResponse.startDate());
        assertEquals(order.getEndDate().toString(), orderResponse.endDate());
        assertEquals(order.getDailyNumberOfVisits(), orderResponse.dailyNumberOfVisits());
        assertEquals(order.getTotalNumberOfVisits(), orderResponse.totalNumberOfVisits());
        assertEquals(order.getCustomer().getUsername(), orderResponse.customerUsername());
        assertEquals(order.getCatsitter().getUsername(), orderResponse.catsitterUsername());
    }

    @Test
    void testOrderRequestToOrder() {
        // Arrange
        OrderRequest orderRequest = OrderRequestFactory.randomOrderRequest().build();

        // Act
        Order order = OrderMapper.OrderRequestToOrder(orderRequest);

        // Assert
        assertEquals(orderRequest.startDate(), order.getStartDate().toString());
        assertEquals(orderRequest.endDate(), order.getEndDate().toString());
        assertEquals(orderRequest.dailyNumberOfVisits(), order.getDailyNumberOfVisits());

        LocalDate startDate = LocalDate.parse(orderRequest.startDate());
        LocalDate endDate = LocalDate.parse(orderRequest.endDate());
        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int expectedTotalNumberOfVisits = (int) (durationInDays * orderRequest.dailyNumberOfVisits());

        assertEquals(expectedTotalNumberOfVisits, order.getTotalNumberOfVisits());
        assertEquals(orderRequest.customerUsername(), order.getCustomer().getUsername());
        assertEquals(orderRequest.catsitterUsername(), order.getCatsitter().getUsername());
    }
}
