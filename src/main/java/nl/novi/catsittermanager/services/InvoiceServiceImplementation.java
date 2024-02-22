package nl.novi.catsittermanager.services;

import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImplementation {


    private final OrderServiceImplementation orderService;

    public InvoiceServiceImplementation(OrderServiceImplementation orderService) {
        this.orderService = orderService;
    }
}
