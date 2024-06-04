package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.OrderRequestFactory;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.OrderFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    @Test
    void testOrderToOrderResponse() {

        // Given
        Order order = OrderFactory.randomOrder().build();

        // When
        OrderResponse orderResponse = OrderMapper.OrderToOrderResponse(order);

        // Then
        assertEquals(order.getStartDate().toString(), orderResponse.startDate());
        assertEquals(order.getEndDate().toString(), orderResponse.endDate());;
        assertEquals(order.getDailyNumberOfVisits(), orderResponse.dailyNumberOfVisits());
        assertEquals(order.getTotalNumberOfVisits(), orderResponse.totalNumberOfVisits());
        assertEquals(order.getCustomer().getUsername(), orderResponse.customerUsername());
        assertEquals(order.getCatsitter().getUsername(), orderResponse.customerUsername());
    }

    @Test
    void testOrderRequestToOrder() {
        // Given
        OrderRequest orderRequest = OrderRequestFactory.randomOrderRequest().build();

        // When
        Order order = OrderMapper.OrderRequestToOrder(orderRequest);

        // Then
        assertEquals(orderRequest.startDate(), order.getStartDate().toString());
        assertEquals(orderRequest.endDate(), order.getEndDate().toString());
        assertEquals(orderRequest.dailyNumberOfVisits(), order.getDailyNumberOfVisits());
        assertEquals(orderRequest.totalNumberOfVisits(), order.getTotalNumberOfVisits());
        assertEquals(orderRequest.customerUsername(), order.getCustomer().getUsername());
        assertEquals(orderRequest.catsitterUsername(), order.getCatsitter().getUsername());
    }
}
