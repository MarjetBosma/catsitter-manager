package nl.novi.catsittermanager.dtos.customer;

import org.springframework.validation.annotation.Validated;

@Validated
public record CustomerInputDto(
        String username,
        String password,
        String name,
        String address,
        String email

) {
}
