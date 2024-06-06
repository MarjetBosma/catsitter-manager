package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.dtos.task.TaskResponse;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.mappers.TaskMapper;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponseList = orderService.getAllOrders().stream()
                .map(OrderMapper::OrderToOrderResponse)
                .toList();
        return ResponseEntity.ok(orderResponseList);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") final UUID idToFind) {
        Order order = orderService.getOrder(idToFind);
        return ResponseEntity.ok(OrderMapper.OrderToOrderResponse(order));
    }

    @GetMapping("/order/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasksByOrder(@PathVariable("id") final UUID idToFind) {
        List<Task> tasks = orderService.getAllTasksByOrder(idToFind);
        List<TaskResponse> taskResponseList = tasks.stream()
                .map(TaskMapper::TaskToTaskResponse)
                .toList();
        return ResponseEntity.ok(taskResponseList);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody final OrderRequest orderRequest) throws URISyntaxException {
        Order order = orderService.createOrder(
                OrderMapper.OrderRequestToOrder(orderRequest),
                orderRequest.customerUsername(),
                orderRequest.catsitterUsername()
        );
        return ResponseEntity.created(new URI("/order/" + order.getOrderNo())).body(OrderMapper.OrderToOrderResponse(order));
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponse> editOrder(@PathVariable("id") final UUID idToEdit, @Valid @RequestBody final OrderRequest orderRequest) {
        Order order = orderService.editOrder(idToEdit, OrderMapper.OrderRequestToOrder(orderRequest), orderRequest.customerUsername(), orderRequest.catsitterUsername());
        return ResponseEntity.ok().body(OrderMapper.OrderToOrderResponse(order));
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") final UUID idToDelete) {
        orderService.deleteOrder(idToDelete);
        return ResponseEntity.ok().body("Order with id " + idToDelete + " removed from database.");
    }
}
