package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.models.Task;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final CatsitterService catsitterService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(final UUID idToFind) {
        return orderRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id."));
    }

    public Order createOrder(final Order order, final String customerUsername, final String catsitterUsername) {
        order.setTasks(new ArrayList<Task>());
        Customer customer = customerService.getCustomer(customerUsername);
        order.setCustomer(customer);
        Catsitter catsitter = catsitterService.getCatsitter(catsitterUsername);
        order.setCatsitter(catsitter);
        return orderRepository.save(order);
    }


// todo: eventueel versie met Validation Exception schrijven


// todo: uitzoeken waarom deze een authentication error geeft.
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

