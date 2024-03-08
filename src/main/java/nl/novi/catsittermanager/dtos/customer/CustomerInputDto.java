package nl.novi.catsittermanager.dtos.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Validated
public record CustomerInputDto (

    @Positive
    Long id,

    int numberOfCats,

    Order order,

    Cat cat,

    Catsitter catsitter

) {}
