package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplementation {

    private final CustomerRepository customerRepos;

    private final CatRepository catRepos;

    private final CatServiceImplementation catService;

    private final CatSitterRepository catSitterRepos;

    private final CatSitterServiceImplementation catSitterService;

    private final OrderRepository orderRepository;

    private final OrderServiceImplementation customerService;

    public CustomerServiceImplementation(CustomerRepository customerRepos, CatRepository catRepos, CatServiceImplementation catService, CatSitterRepository catSitterRepos, CatSitterServiceImplementation catSitterService, OrderRepository orderRepository, OrderServiceImplementation customerService) {
        this.customerRepos = customerRepos;
        this.catRepos = catRepos;
        this.catService = catService;
        this.catSitterRepos = catSitterRepos;
        this.catSitterService = catSitterService;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

}
