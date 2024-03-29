package nl.novi.catsittermanager.dtos.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.order.OrderDto;

import java.util.List;

public record CustomerDto(

        String username,
        String password,
        String name,
        String address,
        String email,
        List<CatResponse> cats,
        @JsonIgnore
        List<OrderDto> orders

) {
}