package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.IdInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/orders")

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
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        if (id > 0) {
            OrderDto orderDto = OrderService.getOrderId();
            return ResponseEntity.ok(orderDto);
        } else {
            throw new RecordNotFoundException("No order found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderInputDto orderInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            OrderDto savedOrder;
            savedOrder = orderService.createOrder(orderInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedOrder.id).toUriString());
            return ResponseEntity.created(uri).body(savedOrder);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable long id, @RequestBody OrderInputDto cat) {
        OrderDto changeOrderId = orderService.updateOrder(id, cat);

        return ResponseEntity.ok().body(changeOrderId);
    }

    @PutMapping("/{id}/catsitter")
    public ResponseEntity<Object> assignCatSitterToOrder(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        orderService.assignCatSitterToOrder(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/customer")
    public ResponseEntity<Object> assignCustomerToOrder(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        orderService.assignCustomerToOrder(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/invoice")
    public ResponseEntity<Object> assignInvoiceToOrder(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        orderService.assignInvoiceToOrder(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/task")
    public ResponseEntity<Object> assignTaskToOrder(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        orderService.assignTaskToOrder(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
