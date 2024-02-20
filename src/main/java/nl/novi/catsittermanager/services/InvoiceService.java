package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.invoice.InvoiceDto;
import nl.novi.catsittermanager.dtos.invoice.InvoiceInputDto;
import nl.novi.catsittermanager.models.Invoice;
import nl.novi.catsittermanager.repositories.InvoiceRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepos;

    private final OrderRepository orderRepos;

    private final OrderService orderService;

    public InvoiceService(InvoiceRepository invoiceRepos, OrderRepository orderRepos, OrderService orderService) {
        this.invoiceRepos = invoiceRepos;
        this.orderRepos = orderRepos;
        this.orderService = orderService;
    }
}
