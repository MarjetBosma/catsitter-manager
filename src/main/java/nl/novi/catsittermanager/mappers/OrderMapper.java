package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.models.Order;

public class OrderMapper {

    public static OrderDto transferToDto(Order order) {
        return new OrderDto(order.getOrderNo(),
                            order.getStartDate(),
                            order.getEndDate(),
                            order.getDailyNumberOfVisits(),
                            order.getTotalNumberOfVisits(),
                            order.getTask(),
                            order.getCustomer(),
                            order.getCatsitter(),
                            order.getInvoice()
        );
    }

    public static Order transferFromDto(OrderInputDto orderInputDto) {
        return Order.builder().orderNo(orderInputDto.orderNo())
                .startDate(orderInputDto.startDate())
                .endDate(orderInputDto.endDate())
                .dailyNumberOfVisits(orderInputDto.dailyNumberOfVisits())
                .totalNumberOfVisits(orderInputDto.totalNumberOfVisits())
                .task(orderInputDto.task())
                .customer(orderInputDto.customer())
                .catsitter(orderInputDto.catsitter())
                .invoice(orderInputDto.invoice())
                .build();
    }
}
