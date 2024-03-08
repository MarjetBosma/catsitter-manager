package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;

import java.util.List;
import java.util.UUID;

public record CatsitterDto(
        UUID id,
        String about,
        List<Order> order,
        List<Customer> customer

) {
}
