package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

@Validated
public record CatsitterInputDto (

        Long id,

        String about,

        Order order,

        Customer customer


) {}

