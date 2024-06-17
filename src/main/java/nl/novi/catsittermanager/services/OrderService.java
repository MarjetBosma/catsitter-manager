package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.models.*;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Task> getAllTasksByOrder(UUID idToFind) {
        Order order = getOrder(idToFind);
        return order.getTasks();
    }

    public Invoice getInvoiceByOrder(UUID idToFind) {
        Order order = getOrder(idToFind);
        if (order.getInvoice() == null) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No invoice found for this order.");
        } else {
            return order.getInvoice();
        }
    }

    public Order createOrder(final Order order, final String customerUsername, final String catsitterUsername) {
        order.setTasks(new ArrayList<Task>());
        Customer customer = customerService.getCustomer(customerUsername);
        order.setCustomer(customer);
        Catsitter catsitter = catsitterService.getCatsitter(catsitterUsername);
        order.setCatsitter(catsitter);
        return orderRepository.save(order);
    }

    public Order editOrder(final UUID idToEdit, final Order updatedOrder, final String customerUsername, final String catsitterUsername) {
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

    public boolean hasExistingInvoice(UUID orderNo) {
        Optional<Order> optionalOrder = orderRepository.findById(orderNo);
        return optionalOrder.map(order -> order.getInvoice() != null).
                orElse(false);
    }
}


