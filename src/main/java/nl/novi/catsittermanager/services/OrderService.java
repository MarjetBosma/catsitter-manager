package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepos;

    public List<OrderDto> getAllOrders() {
        return orderRepos.findAll().stream()
                .map(OrderMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrder(UUID idToFind) {
        return orderRepos.findById(idToFind)
                .map(OrderMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    public OrderDto createOrder(@RequestBody OrderInputDto orderInputDto) {
        Order newOrder = OrderMapper.transferFromInputDto(orderInputDto);
        orderRepos.save(newOrder);
        return OrderMapper.transferToDto(newOrder);
    }

    public OrderDto editOrder(UUID idToEdit, OrderInputDto orderInputDto) {
        Optional<Order> optionalOrder = orderRepos.findById(idToEdit);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (orderInputDto.startDate() != null) {
                order.setStartDate(orderInputDto.startDate());
            }
            if (orderInputDto.endDate() != null) {
                order.setEndDate(orderInputDto.endDate());
            }
            if (orderInputDto.dailyNumberOfVisits() != 0) {
                order.setDailyNumberOfVisits(orderInputDto.dailyNumberOfVisits());
            }
            if (orderInputDto.totalNumberOfVisits() != 0) {
                order.setTotalNumberOfVisits(orderInputDto.totalNumberOfVisits());
            }
            return OrderMapper.transferToDto(order);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id.");
        }
    }

    public UUID deleteOrder(UUID idToDelete) {
        orderRepos.deleteById(idToDelete);
        return idToDelete;
    }

}

