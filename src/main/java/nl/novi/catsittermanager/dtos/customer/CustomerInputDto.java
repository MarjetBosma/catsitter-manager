package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public record CustomerInputDto(
        String username,
        String password,
        String name,
        String address,
        String email,
        List<Order> orders

//        List<Catsitter> catsitters

) {
}
