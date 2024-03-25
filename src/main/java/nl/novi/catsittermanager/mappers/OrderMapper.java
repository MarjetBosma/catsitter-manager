package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;

public class OrderMapper {

    public static OrderDto transferToDto(Order order) {
        return new OrderDto(order.getOrderNo(),
                order.getStartDate(),
                order.getEndDate(),
                order.getDailyNumberOfVisits(),
                order.getTotalNumberOfVisits(),
                order.getTasks(),
                order.getCustomer(),
                order.getCatsitter(),
                order.getInvoice()
        );
    }

    public static Order transferFromInputDto(OrderInputDto orderInputDto) {
        return Order.builder()
                .startDate(orderInputDto.startDate())
                .endDate(orderInputDto.endDate())
                .dailyNumberOfVisits(orderInputDto.dailyNumberOfVisits())
                .totalNumberOfVisits(orderInputDto.totalNumberOfVisits())
                .tasks(new ArrayList<>())
                .customer(new Customer())
                .catsitter(new Catsitter())
                .invoice(new Invoice())
                .build();
    }
}
