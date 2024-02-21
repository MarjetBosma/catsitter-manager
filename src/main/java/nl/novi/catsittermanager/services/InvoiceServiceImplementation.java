package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImplementation {

    private final InvoiceRepository invoiceRepos;

    private final OrderRepository orderRepos;

    private final OrderServiceImplementation orderService;

    public InvoiceServiceImplementation(InvoiceRepository invoiceRepos, OrderRepository orderRepos, OrderServiceImplementation orderService) {
        this.invoiceRepos = invoiceRepos;
        this.orderRepos = orderRepos;
        this.orderService = orderService;
    }
}
