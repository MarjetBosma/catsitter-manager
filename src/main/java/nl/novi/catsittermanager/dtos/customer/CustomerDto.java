package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.models.Catsitter;

import java.util.List;

public record CustomerDto (

        String username,
        String password,
        String name,
        String address,
        String email,
        List<CatDto> cats,
        List<OrderDto> orders

//        List<Catsitter> catsitters

) {
}