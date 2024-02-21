package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImplementation {

    private final OrderRepository orderRepos;

    private final CustomerRepository customerRepos;

    private final CustomerServiceImplementation customerService;

    private final CatSitterRepository catSitterRepos;

    private final CatSitterServiceImplementation catSitterService;

    private final InvoiceRepository invoiceRepos;

    private final InvoiceServiceImplementation invoiceService;


    public OrderServiceImplementation(OrderRepository orderRepos, CustomerRepository customerRepos, CustomerServiceImplementation customerService, CatSitterRepository catSitterRepos, CatSitterServiceImplementation catSitterService, InvoiceRepository invoiceRepos, InvoiceServiceImplementation invoiceService) {
        this.orderRepos = orderRepos;
        this.customerRepos = customerRepos;
        this.customerService = customerService;
        this.catSitterRepos = catSitterRepos;
        this.catSitterService = catSitterService;
        this.invoiceRepos = invoiceRepos;
        this.invoiceService = invoiceService;
    }

}
