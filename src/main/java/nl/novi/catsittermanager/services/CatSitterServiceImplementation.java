package nl.novi.catsittermanager.services;

import org.springframework.stereotype.Service;

@Service
public class CatSitterServiceImplementation {


    private final CustomerServiceImplementation customerService;

    private final OrderServiceImplementation orderService;

    public CatSitterServiceImplementation(CustomerServiceImplementation customerService, OrderServiceImplementation orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }
}
