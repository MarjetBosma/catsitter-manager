package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepos;

    private final CatRepository catRepos;

    private final CatService catService;

    private final CatSitterRepository catSitterRepos;

    private final CatSitterService catSitterService;

    private final OrderRepository orderRepository;

    private final OrderService customerService;

    public CustomerService(CustomerRepository customerRepos, CatRepository catRepos, CatService catService, CatSitterRepository catSitterRepos, CatSitterService catSitterService, OrderRepository orderRepository, OrderService customerService) {
        this.customerRepos = customerRepos;
        this.catRepos = catRepos;
        this.catService = catService;
        this.catSitterRepos = catSitterRepos;
        this.catSitterService = catSitterService;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

}
