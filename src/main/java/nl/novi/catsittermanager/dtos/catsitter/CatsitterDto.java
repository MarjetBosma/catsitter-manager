package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;

public record CatsitterDto(
        Long id,

        String about,

        Order order,

        Customer customer
) {}
