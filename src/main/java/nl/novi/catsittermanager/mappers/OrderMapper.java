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
                            order.getTaskList(),
                            order.getCustomer(),
                            order.getCatsitter(),
                            order.getInvoice()
        );
    }

    public static Order transferFromDto(OrderInputDto orderInputDto) {
        return new Order(orderInputDto.orderNo(),
                         orderInputDto.startDate(),
                         orderInputDto.endDate(),
                         orderInputDto.dailyNumberOfVisits(),
                         orderInputDto.totalNumberOfVisits(),
                         orderInputDto.taskList(),
                         orderInputDto.customer(),
                         orderInputDto.catsitter(),
                         orderInputDto.invoice()
        );
    }
}
