package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders();

    OrderDto getOrder(long idToFind);

    OrderDto createOrder(OrderInputDto orderInputDto);

    OrderDto editOrder(long idToEdit, OrderInputDto orderInputDto);

    String deleteOrder(long idToDelete);
}
