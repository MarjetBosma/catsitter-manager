package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class OrderMapper {

    public static OrderResponse OrderToOrderResponse(Order order) {

        return new OrderResponse(
                order.getOrderNo(),
                order.getStartDate().toString(),
                order.getEndDate().toString(),
                order.getDailyNumberOfVisits(),
                order.getTotalNumberOfVisits(),
                order.getTasks().stream().map(TaskMapper::TaskToTaskResponse).toList(),
                order.getCustomer().getUsername(),
                order.getCatsitter().getUsername(),
                order.getInvoice()
        );
    }

    public static Order OrderRequestToOrder(OrderRequest orderRequest) {

        Customer customer = new Customer();
        customer.setUsername(orderRequest.customerUsername());

        Catsitter catsitter = new Catsitter();
        catsitter.setUsername(orderRequest.catsitterUsername());

        return Order.builder()
                .startDate(LocalDate.parse(orderRequest.startDate()))
                .endDate(LocalDate.parse(orderRequest.endDate()))
                .dailyNumberOfVisits(orderRequest.dailyNumberOfVisits())
                .totalNumberOfVisits(orderRequest.totalNumberOfVisits())
                .tasks(new ArrayList<>())
                .customer(customer)
                .catsitter(catsitter)
                .build();
    }
}
