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
// eventueel versie met Validation Exception schrijven


// todo: uitzoeken waarom deze een authentication error geeft. Andere versie hieronder werkt wel, maar is zo lang, waarom is dit hier allemaal nodig en werkt de korte versie bij andere entities wel?

//    public Order editOrder(UUID idToEdit, Order order) {
//            if (orderRepository.findById(idToEdit).isEmpty()) {
//                throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id.");
//            }
//            return orderRepository.save(order);
//        }

    public Order editOrder(UUID idToEdit, Order updatedOrder, final String customerUsername, final String catsitterUsername) {
        Order existingOrder = orderRepository.findById(idToEdit)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No order found with this id."));

        existingOrder.setStartDate(updatedOrder.getStartDate());
        existingOrder.setEndDate(updatedOrder.getEndDate());
        existingOrder.setDailyNumberOfVisits(updatedOrder.getDailyNumberOfVisits());
        existingOrder.setTotalNumberOfVisits(updatedOrder.getTotalNumberOfVisits());

        Catsitter catsitter = updatedOrder.getCatsitter();
        if (catsitter != null) {
            Catsitter existingCatsitter = catsitterService.getCatsitter(catsitterUsername);
            existingOrder.setCatsitter(existingCatsitter);
        }
        Customer customer = updatedOrder.getCustomer();
        if (customer != null) {
            Customer existingCustomer = customerService.getCustomer(customerUsername);
            existingOrder.setCustomer(existingCustomer);
        }
        return orderRepository.save(existingOrder);
    }


    public UUID deleteOrder(UUID idToDelete) {
        if (!orderRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No order found with this id.");
        }
        orderRepository.deleteById(idToDelete);
        return idToDelete;
    }

}

