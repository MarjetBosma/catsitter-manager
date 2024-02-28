package nl.novi.catsittermanager.dtos.catsitter;

import org.springframework.validation.annotation.Validated;

@Validated
public record CatsitterInputDto (

    String about,

//    Order orderList = new ArrayList<>();,
    String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Customer customerList = new ArrayList<>();
    String customerList //Dummy, alleen voor los testen van de klasse in Postman zonder database

) {}

