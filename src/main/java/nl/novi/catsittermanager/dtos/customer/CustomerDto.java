package nl.novi.catsittermanager.dtos.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Order;

import java.util.List;

public record CustomerDto(

        String username,
        String password,
        String name,
        String address,
        String email,
        List<CatDto> cats,
        @JsonIgnore
        List<OrderDto> orders

) {
}