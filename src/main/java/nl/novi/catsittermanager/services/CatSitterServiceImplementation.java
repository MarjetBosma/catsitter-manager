package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CatSitterServiceImplementation {

    private final CatSitterRepository catSitterRepos;

    private final CustomerRepository customerRepository;

    private final CustomerServiceImplementation customerService;

    private final OrderRepository orderRepository;

    private final OrderServiceImplementation orderService;

    public CatSitterServiceImplementation(CatSitterRepository catSitterRepos, CustomerRepository customerRepository, CustomerServiceImplementation customerService, OrderRepository orderRepository, OrderServiceImplementation orderService) {
        this.catSitterRepos = catSitterRepos;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }
}
