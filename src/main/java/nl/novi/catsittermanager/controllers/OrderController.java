package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/order")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") final UUID idToFind) {
        OrderDto orderDto = orderService.getOrder(idToFind);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody final OrderInputDto orderInputDto, final BindingResult br) {
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
    public ResponseEntity<OrderDto> editOrder(@PathVariable("id") final UUID idToEdit, @RequestBody final OrderInputDto order) {
        OrderDto changeOrderId = orderService.editOrder(idToEdit, order);
        return ResponseEntity.ok().body(changeOrderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") final UUID idToDelete) {
        orderService.deleteOrder(idToDelete);
        return ResponseEntity.ok().body("Order with id " + idToDelete + " removed from database");
    }

}
