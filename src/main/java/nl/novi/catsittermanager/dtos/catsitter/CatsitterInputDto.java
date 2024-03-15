package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public record CatsitterInputDto (
        String username,
        String password,
        String name,
        String address,
        String email,
        String about,
        List<Order> orders

//        List<Customer> customers

) {
}

