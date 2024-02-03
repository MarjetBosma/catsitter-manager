package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.dtos.order.OrderInputDto;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepos;

    private final CustomerRepository customerRepos;

    private final CustomerService customerService;

    private final CatSitterRepository catSitterRepos;

    private final CatSitterService catSitterService;

    private final InvoiceRepository invoiceRepos;

    private final InvoiceService invoiceService;


    public OrderService(OrderRepository orderRepos, CustomerRepository customerRepos, CustomerService customerService, CatSitterRepository catSitterRepos, CatSitterService catSitterService, InvoiceRepository invoiceRepos, InvoiceService invoiceService) {
        this.orderRepos = orderRepos;
        this.customerRepos = customerRepos;
        this.customerService = customerService;
        this.catSitterRepos = catSitterRepos;
        this.catSitterService = catSitterService;
        this.invoiceRepos = invoiceRepos;
        this.invoiceService = invoiceService;
    }

    public OrderDto transferToDto(Order order) {

        OrderDto orderDto = new OrderDto();

        orderDto.orderNo = order.getOrderNo();
        orderDto.startDate = order.getStartDate();
        orderDto.endDate = order.getEndDate();
        orderDto.dailyNumberOfVisits = order.getDailyNumberOfVisits();
        orderDto.totalNumberOfVisits = order.getTotalNumberOfVisits();
        orderDto.taskList = order.getTaskList();
        orderDto.customer =  order.getCustomer();
        orderDto.catSitter = order.getCatSitter();
        orderDto.invoice = order.getInvoice();

        return orderDto;
    }

    public Order transferToOrder(OrderInputDto orderDto) {

        Order order = new Order();

        order.setStartDate(orderDto.startDate);
        order.setEndDate(orderDto.endDate);
        order.setDailyNumberOfVisits(orderDto.dailyNumberOfVisits);
        order.setTotalNumberOfVisits(orderDto.totalNumberOfVisits);
        order.setTaskList(orderDto.taskList);
        order.setCustomer(orderDto.customer);
        order.setCatSitter(orderDto.catSitter);
        order.setInvoice(orderDto.invoice);

        return order;

    }

}
