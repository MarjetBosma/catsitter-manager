package nl.novi.catsittermanager.dtos.catsitter;

import org.springframework.validation.annotation.Validated;


@Validated
public record CatsitterRequest(
        String username,
        String password,
        String name,
        String address,
        String email,
        String about

) {
}

