package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;

import java.util.List;
import java.util.UUID;

public record CustomerDto (

        //TODO add user fields

        List<Order> order,
        List<Cat> cats,
        List<Catsitter> catsitter

) {
}