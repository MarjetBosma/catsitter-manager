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

}
