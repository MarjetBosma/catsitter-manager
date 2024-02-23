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
                            order.getCatSitter(),
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
                         orderInputDto.catSitter(),
                         orderInputDto.invoice()
        );
    }
//    public OrderDto transferToDto(Order order) {
//
//        OrderDto orderDto = new OrderDto();
//
//        orderDto.orderNo = order.getOrderNo();
//        orderDto.startDate = order.getStartDate();
//        orderDto.endDate = order.getEndDate();
//        orderDto.dailyNumberOfVisits = order.getDailyNumberOfVisits();
//        orderDto.totalNumberOfVisits = order.getTotalNumberOfVisits();
//        orderDto.taskList = order.getTaskList();
//        orderDto.customer =  order.getCustomer();
//        orderDto.catSitter = order.getCatSitter();
//        orderDto.invoice = order.getInvoice();
//
//        return orderDto;
//    }
//
//    public Order transferToOrder(OrderInputDto orderDto) {
//
//        Order order = new Order();
//
//        order.setStartDate(orderDto.startDate);
//        order.setEndDate(orderDto.endDate);
//        order.setDailyNumberOfVisits(orderDto.dailyNumberOfVisits);
//        order.setTotalNumberOfVisits(orderDto.totalNumberOfVisits);
//        order.setTaskList(orderDto.taskList);
//        order.setCustomer(orderDto.customer);
//        order.setCatSitter(orderDto.catSitter);
//        order.setInvoice(orderDto.invoice);
//
//        return order;
//    }
}
