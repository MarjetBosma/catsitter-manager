package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

        LocalDate startDate = LocalDate.parse(orderRequest.startDate());
        LocalDate endDate = LocalDate.parse(orderRequest.endDate());

        int dailyNumberOfVisits = orderRequest.dailyNumberOfVisits();
        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int totalNumberOfVisits = (int) durationInDays * dailyNumberOfVisits;

        List<Task> tasks = new ArrayList<>();

        Order order = Order.builder()
                .startDate(startDate)
                .endDate(endDate)
                .dailyNumberOfVisits(dailyNumberOfVisits)
                .totalNumberOfVisits(totalNumberOfVisits)
                .tasks(tasks)
                .customer(customer)
                .catsitter(catsitter)
                .build();

        for (Task task : tasks) {
            task.setOrder(order);
        }

        return order;
    }
}
