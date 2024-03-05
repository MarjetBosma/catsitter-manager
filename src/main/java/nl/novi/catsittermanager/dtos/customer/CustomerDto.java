package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;

import java.util.List;

public record CustomerDto (

        Long id,

        int numberOfCats,

        Order order,

        Cat cats,

        Catsitter catsitter

) {}