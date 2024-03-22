package nl.novi.catsittermanager.dtos.catsitter;

import java.util.List;

public record CatsitterDto(

        String username,
        String password,
        String name,
        String address,
        String email,
        String about,
        List<nl.novi.catsittermanager.dtos.order.OrderDto> customers

) {
}
