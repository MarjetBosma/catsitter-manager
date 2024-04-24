package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OrderMapper {

    public static OrderResponse OrderToOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderNo(),
                order.getStartDate(),
                order.getEndDate(),
                order.getDailyNumberOfVisits(),
                order.getTotalNumberOfVisits(),
                order.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList()
        );
    }

    public static Order OrderRequestToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .startDate(orderRequest.startDate())
                .endDate(orderRequest.endDate())
                .dailyNumberOfVisits(orderRequest.dailyNumberOfVisits())
                .totalNumberOfVisits(orderRequest.totalNumberOfVisits())
                .tasks(new ArrayList<>())
                .customer(new Customer())
                .catsitter(new Catsitter())
                .build();
    }
}
