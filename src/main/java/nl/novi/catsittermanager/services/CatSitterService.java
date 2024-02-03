package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.models.CatSitter;
import nl.novi.catsittermanager.repositories.CatSitterRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import nl.novi.catsittermanager.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CatSitterService {

    private final CatSitterRepository catSitterRepos;

    private final CustomerRepository customerRepository;

    private final CustomerService customerService;

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    public CatSitterService(CatSitterRepository catSitterRepos, CustomerRepository customerRepository, CustomerService customerService, OrderRepository orderRepository, OrderService orderService) {
        this.catSitterRepos = catSitterRepos;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public CatSitterDto transferToDto(CatSitter catSitter) {

        CatSitterDto catSitterDto = new CatSitterDto();

        // catSitterDto.id = catSitter.getId(); // deze komt uit superklasse User
        catSitterDto.about = catSitter.getAbout();
        catSitterDto.customers = catSitter.getCustomers();
        catSitterDto.orderList = catSitter.getOrderList();

        return catSitterDto;
    }

    public CatSitter transferToCatSitter(CatSitterInputDto catSitterDto) {

        CatSitter catSitter = new CatSitter();

        catSitter.setAbout(catSitterDto.about);
        catSitter.setCustomers(catSitterDto.customers);
        catSitter.setOrderList(catSitterDto.orderList);
    }
}
