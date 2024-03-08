package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.OrderRepository;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepos;
    private final InvoiceRepository invoiceRepos;

    public OrderServiceImplementation(OrderRepository orderRepos, InvoiceRepository invoiceRepos) {
        this.orderRepos = orderRepos;
        this.invoiceRepos = invoiceRepos;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<Order> orderList = orderRepos.findAll();

        for (Order order : orderList) {
            OrderDto orderDto = OrderMapper.transferToDto(order);
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    @Override
    public OrderDto getOrder(long idToFind) {
        Optional<Order> orderOptional = orderRepos.findById(idToFind);
            if (orderOptional.isPresent()) {
                return OrderMapper.transferToDto(orderOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id.");
        }
    }

    @Override
    public OrderDto createOrder(@RequestBody OrderInputDto orderInputDto) {
        Order newOrder = new Order();
        newOrder.setStartDate(orderInputDto.startDate());
        newOrder.setEndDate(orderInputDto.endDate());
        newOrder.setDailyNumberOfVisits(orderInputDto.dailyNumberOfVisits());
        newOrder.setTotalNumberOfVisits(orderInputDto.totalNumberOfVisits());
        newOrder.setTaskList(orderInputDto.taskList());
        newOrder.setCustomer(orderInputDto.customer());
        newOrder.setCatsitter(orderInputDto.catsitter());
        newOrder.setInvoice(orderInputDto.invoice());
        orderRepos.save(newOrder);
        return OrderMapper.transferToDto(newOrder);
    }

    @Override
    public OrderDto editOrder(long idToEdit, OrderInputDto orderInputDto) {
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
                if (orderInputDto.taskList() != null) {
                    order.setTaskList(orderInputDto.taskList());
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
    public long deleteOrder(long idToDelete) {
        Optional<Order> optionalOrder = orderRepos.findById(idToDelete);
        if (optionalOrder.isPresent()) {
            orderRepos.deleteById(idToDelete);
            return idToDelete;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id");
        }
    }

    @Override
    public OrderDto assignInvoiceToOrder(long customerId, long invoiceId) {
        Optional<Order> optionalOrder = orderRepos.findById(customerId);
        Optional<Invoice> optionalInvoice = invoiceRepos.findById(invoiceId);

        if (optionalOrder.isPresent() && optionalInvoice.isPresent()) {
            Order order = optionalOrder.get();
            Invoice invoice = optionalInvoice.get();

            order.setInvoice(invoice);
            orderRepos.save(order);
            return OrderMapper.transferToDto(order);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order or invoice found with this id");
        }
    }
}

