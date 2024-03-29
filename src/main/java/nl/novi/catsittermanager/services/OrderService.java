package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepos;
    private final CustomerRepository customerRepos;
    private final CatsitterRepository catsitterRepos;

    public List<OrderDto> getAllOrders() {
        return orderRepos.findAll().stream()
                .map(OrderMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrder(UUID idToFind) {
        return orderRepos.findById(idToFind)
                .map(OrderMapper::transferToDto)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id."));
    }

    public OrderDto createOrder(@RequestBody OrderInputDto orderInputDto) {
        Order newOrder = OrderMapper.transferFromInputDto(orderInputDto);
        newOrder.setTasks(new ArrayList<Task>());
        Customer customer = customerRepos.findById(orderInputDto.customerUsername())
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Customer not found"));
        newOrder.setCustomer(customer);
        Catsitter catsitter = catsitterRepos.findById(orderInputDto.catsitterUsername())
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Catsitter not found"));
        newOrder.setCatsitter(catsitter);
        newOrder.setInvoice(new Invoice());
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
            if (orderInputDto.customerUsername() != null) {
                Customer customer = customerRepos.findById(orderInputDto.customerUsername())
                        .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Customer not found"));
                order.setCustomer(customer);
            }
            if (orderInputDto.catsitterUsername() != null) {
                Catsitter catsitter = catsitterRepos.findById(orderInputDto.catsitterUsername())
                        .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Catsitter not found"));
                order.setCatsitter(catsitter);
            }
            return OrderMapper.transferToDto(order);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id.");
        }
    }

    public UUID deleteOrder(UUID idToDelete) {
        orderRepos.deleteById(idToDelete);
        return idToDelete;

    }

}

