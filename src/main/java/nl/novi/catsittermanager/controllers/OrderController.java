package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.OrderServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/order")

public class OrderController {

    private final OrderServiceImplementation orderService;

    public OrderController(OrderServiceImplementation orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") long idToFind) {
        if (idToFind > 0) {
            OrderDto orderDto = orderService.getOrder(idToFind);
            return ResponseEntity.ok(orderDto);
        } else {
            throw new RecordNotFoundException("No order found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderInputDto orderInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            OrderDto savedOrder;
            savedOrder = orderService.createOrder(orderInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedOrder).toUriString());
            return ResponseEntity.created(uri).body(savedOrder);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> editOrder(@PathVariable("id") long idToEdit, @RequestBody OrderInputDto order) {
        OrderDto changeOrderId = orderService.editOrder(idToEdit, order);
        return ResponseEntity.ok().body(changeOrderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") long idToDelete) {
        orderService.deleteOrder(idToDelete);
        return ResponseEntity.ok().body("Order with id " + idToDelete +  " removed from database");
    }

    @PutMapping("/{orderId}/{invoiceId}")
    public OrderDto assignInvoiceToOrder(@PathVariable("orderId") long orderId, @PathVariable("invoiceId") long invoiceId) {
        return orderService.assignInvoiceToOrder(orderId, invoiceId);
    }

    @PutMapping("/{orderId}/{taskId}")
    public OrderDto assignTaskToOrder(@PathVariable("personId") long orderId, @PathVariable("taskId") long taskId) {
        return orderService.assignTaskToOrder(orderId, taskId);
    }
}
