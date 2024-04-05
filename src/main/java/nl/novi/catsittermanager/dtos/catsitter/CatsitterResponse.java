package nl.novi.catsittermanager.dtos.catsitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.catsittermanager.dtos.order.OrderResponse;

import java.util.List;

public record CatsitterResponse(

        String username,
        String password,
        String name,
        String address,
        String email,
        String about,
        List<OrderResponse> orders

) {
}
