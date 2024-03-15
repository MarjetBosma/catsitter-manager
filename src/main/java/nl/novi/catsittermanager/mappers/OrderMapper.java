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
                            order.getTasks(),
                            order.getCustomers(),
                            order.getCatsitters(),
                            order.getInvoice()
        );
    }

    public static Order transferFromDto(OrderInputDto orderInputDto) {
        return Order.builder().orderNo(orderInputDto.orderNo())
                .startDate(orderInputDto.startDate())
                .endDate(orderInputDto.endDate())
                .dailyNumberOfVisits(orderInputDto.dailyNumberOfVisits())
                .totalNumberOfVisits(orderInputDto.totalNumberOfVisits())
                .tasks(orderInputDto.task())
                .customers(orderInputDto.customer())
                .catsitters(orderInputDto.catsitter())
                .invoice(orderInputDto.invoice())
                .build();
    }
}
