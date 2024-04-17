package nl.novi.catsittermanager.dtos.catsitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.catsittermanager.dtos.order.OrderResponse;

import java.util.List;

public record CatsitterResponse(

        String username,
        String name,
        String address,
        String email,
        String about,
        @JsonIgnore
        List<OrderResponse> orders

) {
}
