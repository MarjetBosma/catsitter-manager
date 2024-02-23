package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.mappers.OrderMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

//    private final OrderRepository orderRepos;
//
//    private final TaskRepository taskRepos;
//
//    private final TaskImplementationService;
//
//    private final CustomerRepository customerRepos;
//
//    private final CustomerServiceImplementation customerService;
//
//    private final CatSitterRepository catSitterRepos;
//
//    private final CatSitterServiceImplementation catSitterService;
//
//    private final InvoiceRepository invoiceRepos;
//
//    private final InvoiceServiceImplementation invoiceService;

    private List<Order> orders = new ArrayList<>(); // voor testen zonder database

//    public OrderServiceImplementation(OrderRepository orderRepos, TaskRepository taskRepos, TaskServiceImplementation taskService, CustomerRepository customerRepos, CustomerServiceImplementation customerService, CatSitterRepository catSitterRepos, CatSitterServiceImplementation catSitterService, InvoiceRepository invoiceRepos, InvoiceServiceImplementation invoiceService)) {
//        this.orderRepos = catRepos;
//        this.taskRepos = taskRepos;
//        this.taskService = taskService;
//        this.customerRepos = customerRepos;
//        this.customerService = customerService;
//        this.invoiceRepos = invoiceRepos;
//        this.invoiceService = invoiceService;
//    }

    public OrderServiceImplementation() { // Bedoeld voor testen zonder database
        orders.add(new Order(1L, LocalDate.parse("2023-08-15"), LocalDate.parse("2023-08-30"), 2, 42, "takenlijst", "Marjet Bosma", "Karel Appel", "factuur"));
        orders.add(new Order(1L, LocalDate.parse("2024-01-15"), LocalDate.parse("2024-02-15"), 1, 27, "takenlijst", "Marianne Bosma", "Liesje Peer", "factuur"));
    }

    @Override
    public List<OrderDto> getAllOrders() {
//        List<Order> orderList = orderRepos.findAll(); // Deze is voor als de database gevuld is
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = OrderMapper.transferToDto(order);
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    @Override
    public OrderDto getOrder(long idToFind) {
        for (Order order : orders) {
            if (order.getOrderNo() == idToFind) {
                return OrderMapper.transferToDto(order);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id.");
    }

    @Override
    public OrderDto createOrder(OrderInputDto orderInputDto) {
        Order newOrder = OrderMapper.transferFromDto(orderInputDto);
        orders.add(newOrder);
        return OrderMapper.transferToDto(newOrder);
    }

    @Override
    public OrderDto editOrder(long idToEdit, OrderInputDto orderInputDto) {
        for (Order order : orders) {
            if (order.getOrderNo() == idToEdit) {
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
                if (orderInputDto.catSitter() != null) {
                    order.setCatSitter(orderInputDto.catSitter());
                }
                if (orderInputDto.invoice() != null) {
                    order.setInvoice(orderInputDto.invoice());
                }
                return OrderMapper.transferToDto(order);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id.");
    }

    @Override
    public void deleteOrder(long idToDelete) {
        for (Order order : orders) {
            if (order.getOrderNo() == idToDelete) {
                orders.remove(order);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with this id");
    }
}

// methodes schrijven om Order aan andere entiteiten te koppelen