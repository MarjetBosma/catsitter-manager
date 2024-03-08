package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
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
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepos;

    public OrderServiceImplementation(OrderRepository orderRepos) {
        this.orderRepos = orderRepos;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepos.findAll().stream()
                .map(OrderMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrder(UUID idToFind) {
        return orderRepos.findById(idToFind)
                .map(OrderMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    @Override
    public OrderDto createOrder(@RequestBody OrderInputDto orderInputDto) {
        Order newOrder = new Order();
        newOrder.setStartDate(orderInputDto.startDate());
        newOrder.setEndDate(orderInputDto.endDate());
        newOrder.setDailyNumberOfVisits(orderInputDto.dailyNumberOfVisits());
        newOrder.setTotalNumberOfVisits(orderInputDto.totalNumberOfVisits());
        newOrder.setTask(orderInputDto.task());
        newOrder.setCustomer(orderInputDto.customer());
        newOrder.setCatsitter(orderInputDto.catsitter());
        newOrder.setInvoice(orderInputDto.invoice());
        orderRepos.save(newOrder);
        return OrderMapper.transferToDto(newOrder);
    }

    @Override
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
                if (orderInputDto.task() != null) {
                    order.setTask(orderInputDto.task());
                }
                if (orderInputDto.customer() != null) {
                    order.setCustomer(orderInputDto.customer());
                }
                if (orderInputDto.catsitter() != null) {
                    order.setCatsitter(orderInputDto.catsitter());
                }
                if (orderInputDto.invoice() != null) {
                    order.setInvoice(orderInputDto.invoice());
                }
                return OrderMapper.transferToDto(order);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id.");
        }
    }

    @Override
    public UUID deleteOrder(UUID idToDelete) {
        orderRepos.deleteById(idToDelete);
        return idToDelete;
    }

//    @Override
//    public OrderDto assignInvoiceToOrder(long customerId, long invoiceId) {
//        Optional<Order> optionalOrder = orderRepos.findById(customerId);
//        Optional<Invoice> optionalInvoice = invoiceRepos.findById(invoiceId);
//
//        if (optionalOrder.isPresent() && optionalInvoice.isPresent()) {
//            Order order = optionalOrder.get();
//            Invoice invoice = optionalInvoice.get();
//
//            order.setInvoice(invoice);
//            orderRepos.save(order);
//            return OrderMapper.transferToDto(order);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order or invoice found with this id");
//        }
//    }
//
//    @Override
//    public OrderDto assignTaskToOrder(long orderId, long taskId) {
//        Optional<Order> optionalOrder = orderRepos.findById(orderId);
//        Optional<Task> optionalTask= taskRepos.findById(taskId);
//
//        if (optionalOrder.isPresent() && optionalTask.isPresent()) {
//            Order order = optionalOrder.get();
//            Task task = optionalTask.get();
//
//            order.setTask(task);
//            orderRepos.save(order);
//            return OrderMapper.transferToDto(order);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order or task found with this id");
//        }
//    }
}

