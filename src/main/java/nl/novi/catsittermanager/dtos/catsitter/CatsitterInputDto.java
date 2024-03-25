package nl.novi.catsittermanager.dtos.catsitter;

import org.springframework.validation.annotation.Validated;


@Validated
public record CatsitterInputDto(
        String username,
        String password,
        String name,
        String address,
        String email,
        String about

) {
}

