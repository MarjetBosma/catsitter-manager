package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getAllOrders();

    OrderDto getOrder(UUID idToFind);

    OrderDto createOrder(OrderInputDto orderInputDto);

    OrderDto editOrder(UUID idToEdit, OrderInputDto orderInputDto);

    UUID deleteOrder(UUID idToDelete);

}
