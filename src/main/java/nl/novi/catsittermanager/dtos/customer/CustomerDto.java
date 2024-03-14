package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;

import java.util.List;
import java.util.UUID;

public record CustomerDto (

        //TODO add user fields
        String username,
        String password,
        String name,
        String address,
        String email,
        List<CatDto> cats

) {
}