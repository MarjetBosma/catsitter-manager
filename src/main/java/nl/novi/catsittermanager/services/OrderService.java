package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import nl.novi.catsittermanager.dtos.order.OrderRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CatsitterRepository catsitterRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(UUID idToFind) {
        return orderRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id."));
    }

    public Order createOrder(final Order order) {
        order.setTasks(new ArrayList<Task>());
        order.setInvoice(new Invoice());
        // customers en catsitters hier toevoegen of juist niet?
        return orderRepository.save(order);
    }

// todo: uitzoeken hoe ik het beste customers en catsitters kan toevoegen zonder een loop te creeren. Hieronder oude versie via orderRequest.

//    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
//        Order order = OrderMapper.OrderRequestToOrder(orderRequest);
//        order.setTasks(new ArrayList<Task>());
//        Customer customer = customerRepository.findById(orderRequest.customerUsername())
//                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Customer not found"));
//        order.setCustomer(customer);
//        Catsitter catsitter = catsitterRepository.findById(orderRequest.catsitterUsername())
//                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Catsitter not found"));
//        order.setCatsitter(catsitter);
//        order.setInvoice(new Invoice());
//        orderRepository.save(order);
//        return OrderMapper.OrderToOrderResponse(order);
//    }

// todo: eventueel versie met Validation Exception schrijven


// todo: uitzoeken waarom deze een 500 error geeft, mogelijk iets met de gerelateerde klassen?
    public Order editOrder(UUID idToEdit, Order order) {
            if (orderRepository.findById(idToEdit).isEmpty()) {
                throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id.");
            }
            return orderRepository.save(order);
        }

    public UUID deleteOrder(UUID idToDelete) {
        if (!orderRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No order found with this id.");
        }
        orderRepository.deleteById(idToDelete);
        return idToDelete;
    }

}

